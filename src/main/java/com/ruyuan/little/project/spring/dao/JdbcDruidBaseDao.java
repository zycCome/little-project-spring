package com.ruyuan.little.project.spring.dao;

import com.ruyuan.little.project.spring.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:jdbc采用Druid连接池操作dao组件
 **/
public class JdbcDruidBaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDruidBaseDao.class);

    private DataSource dataSource;

    public JdbcDruidBaseDao(){

    }

    public JdbcDruidBaseDao(DataSource dataSource){
        super();
        this.dataSource = dataSource;
    }

    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭数据库连接
     * @param connection 数据库连接
     */
    private void closeConnection(Connection connection) {
        CloseableUtils.close(connection);
    }


    /**
     * 根据sql查询列表数据
     *
     * @param sql         sql
     * @param params      参数
     * @param resultClazz 结果类型
     * @param <T>         返回列表泛型
     * @return 列表结果
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> List<T> queryList(String sql, Object[] params, Class<T> resultClazz) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(sql);
            resultSet = executeQuery(preparedStatement,params);
            while (resultSet.next()) {
                T o = resultClazz.newInstance();
                Field[] fields = resultClazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    try {
                        Object value = resultSet.getObject(fieldName);
                        field.set(o, value);
                    }catch (SQLException e){
                        LOGGER.debug("结果集中无 {} 参数", fieldName);
                    }
                }
                result.add(o);
            }
        } finally {
            CloseableUtils.close(resultSet);
            CloseableUtils.close(preparedStatement);
            closeConnection(conn);
        }
        return result;
    }

    /**
     * 查询单条数据
     * @param sql 查询sql
     * @param params 参数
     * @param resultClazz 结果对象class
     * @param <T> 结果对象泛型
     * @return 查询结果
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T queryOne(String sql, Object[] params, Class<T> resultClazz) throws SQLException, InstantiationException, IllegalAccessException {
        T o = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(sql);
            resultSet = executeQuery(preparedStatement,params);
            if (resultSet.next()) {
                o = resultClazz.newInstance();
                Field[] fields = resultClazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = resultSet.getObject(field.getName());
                        field.set(o, value);
                    }catch (SQLException e){
                        LOGGER.debug("结果集中无 {} 参数",field.getName());
                    }
                }
                if (resultSet.next()) {
                    throw new RuntimeException("结果集不唯一");
                }
            }
        } finally {
            CloseableUtils.close(resultSet);
            CloseableUtils.close(preparedStatement);
            closeConnection(conn);
        }
        return o;
    }

    /**
     * 增删改数据
     * @param sql 待执行sql
     * @param params 参数
     * @return 增删改结果
     * @throws SQLException
     */
    public int update(String sql, Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeUpdate();
        } finally {
            CloseableUtils.close(preparedStatement);
            closeConnection(conn);
        }
    }



    /**
     * 拼接参数 执行查询
     * @param preparedStatement jdbc的sql处理对象
     * @param params 请求参数
     * @return 查询结果
     * @throws SQLException
     */
    private ResultSet executeQuery(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        return preparedStatement.executeQuery();
    }
}