package com.ruyuan.little.project.spring.dao;

import com.ruyuan.little.project.spring.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:通过jdbc Template基础dao组件
 **/
public class TemplateBaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateBaseDao.class);

    /**
     * jdbc的Template
     */
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 更新
     *
     * @param sql    更新sql
     * @param params 参数
     * @return 结果
     */
    public int templateUpdate(String sql, Object[] params) {
        if (params == null) {
            return this.jdbcTemplate.update(sql);
        }
        return this.jdbcTemplate.update(sql, params);
    }

    /**
     * 查询
     *
     * @param sql    查询sql
     * @param params 参数
     * @return 结果
     */
    public List<Map<String, Object>> templateQuery(String sql, Object[] params) {
        if (params == null) {
            return this.jdbcTemplate.queryForList(sql);
        }
        return this.jdbcTemplate.queryForList(sql, params);
    }

    /**
     * 查询一条数据
     *
     * @param sql    查询sql
     * @param params 参数
     * @param clazz  结果类型
     * @param <T>    结果类型泛型
     * @return 结果
     */
    public <T> T templateQueryOne(String sql, Object[] params, Class<T> clazz) {
        RowMapper<T> tRowMapper = (rs, rowNum) -> {
            T o = null;
            try {
                o = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    try {
                        Object value = rs.getObject(fieldName);
                        field.set(o, value);
                    }catch (SQLException e){
                        LOGGER.debug("结果集中无 {} 参数", fieldName);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("异常信息:{}", e);
            }
            return o;
        };
        try {
            if (params == null) {
                return this.jdbcTemplate.queryForObject(sql, tRowMapper);
            }
            return this.jdbcTemplate.queryForObject(sql, params, tRowMapper);
        }catch (DataAccessException e){
            LOGGER.error("数据查询失败",e);
            throw new BusinessException("查询失败");
        }
    }


}
