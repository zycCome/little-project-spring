package com.ruyuan.little.project.spring.dao;

import com.ruyuan.little.project.spring.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:jdbc操作dao组件
 **/
public class JdbcBaseDao {

    /**
     * 日志组件
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBaseDao.class);

    /**
     * 数据库驱动
     */
    private String driverClassName;

    /**
     * url地址
     */
    private String url;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;


    /**
     * 根据sql查询列表数据
     *
     * @param sql         sql
     * @param params      参数
     * @param resultClazz 结果类型
     * @param <T>         返回列表返现
     * @return 列表结果
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> List<T> queryList(String sql, Object[] params, Class<T> resultClazz) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, username, password);
            preparedStatement = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
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
            conn.close();
        } finally {
            CloseableUtils.close(resultSet);
            CloseableUtils.close(preparedStatement);
            CloseableUtils.close(conn);
        }
        return result;
    }

    public <T> T queryOne(String sql, Object[] params, Class<T> resultClazz) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        T o = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, username, password);
            preparedStatement = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
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
            CloseableUtils.close(conn);
        }
        return o;
    }

    public int update(String sql, Object[] params) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, username, password);
            preparedStatement = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeUpdate();
        } finally {
            CloseableUtils.close(preparedStatement);
            CloseableUtils.close(conn);
        }
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
