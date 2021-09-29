package com.ruyuan.little.project.spring.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.spring.dao.JdbcBaseDao;
import com.ruyuan.little.project.spring.dao.JdbcDruidBaseDao;
import com.ruyuan.little.project.spring.dao.TemplateBaseDao;
import com.ruyuan.little.project.spring.dto.Teacher;
import com.ruyuan.little.project.spring.mapper.TeacherMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:spring实战 测试调试用
 **/
@RestController
@RequestMapping(value = "/demo")
public class JdbcDemoController {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private TemplateBaseDao templateBaseDao;

    @Autowired
    private JdbcBaseDao jdbcBaseDao;

    @Autowired
    private JdbcDruidBaseDao jdbcDruidBaseDao;

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private TeacherMapper teacherMapper;

    @RequestMapping("/jdbc/insert")
    public CommonResponse insert(){
        String sql = "INSERT INTO spring.t_teacher (id, teacher_name, course, score, teaching_days, status, teaching_count, price_of_day, start_time, description, photo, detail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {100,"张三","00",4.8,4,"1",86,200.0, null,"专注于6-10岁儿童数学教育",null,"常年做儿童教育，经验丰富"};
        int update = 0;
        try {
            update = jdbcBaseDao.update(sql, params);
        } catch (Exception e) {
            log.error("添加教师记录失败",e);
        }
        return CommonResponse.success("成功插入 "+ update + " 条教师记录");
    }

    @RequestMapping("/jdbcDruid/insert")
    public CommonResponse jdbcDruidInsert(){
        String sql = "INSERT INTO spring.t_teacher (id, teacher_name, course, score, teaching_days, status, teaching_count, price_of_day, start_time, description, photo, detail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {102,"张三","00",4.8,4,"1",86,200.0, null,"专注于6-10岁儿童数学教育",null,"常年做儿童教育，经验丰富"};
        int update = 0;
        try {
            update = jdbcDruidBaseDao.update(sql, params);
        } catch (Exception e) {
            log.error("添加教师记录失败",e);
        }
        return CommonResponse.success("成功插入 "+ update + " 条教师记录");
    }

    @RequestMapping("/template/insert")
    public CommonResponse templateInsert(){
        String sql = "INSERT INTO spring.t_teacher (id, teacher_name, course, score, teaching_days, status, teaching_count, price_of_day, start_time, description, photo, detail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {101,"张三","00",4.8,4,"1",86,200.0, null,"专注于6-10岁儿童数学教育",null,"常年做儿童教育，经验丰富"};
        int update = 0;
        try {
            update = templateBaseDao.templateUpdate(sql, params);
        } catch (Exception e) {
            log.error("添加教师记录失败",e);
        }
        return CommonResponse.success("成功插入 "+ update + " 条教师记录");
    }

    @RequestMapping("/jdbc/queryOne")
    public CommonResponse queryOne(){
        String sql = "select * from spring.t_teacher where id = ?";
        Object[] params = {100};
        Teacher teacher = null;
        try {
            teacher = jdbcBaseDao.queryOne(sql, params, Teacher.class);
        } catch (Exception e) {
            log.error("查询教师详情失败",e);
        }
        return CommonResponse.success(teacher);
    }

    @RequestMapping("/jdbcDruid/queryOne")
    public CommonResponse jdbcDruidQueryOne(){
        String sql = "select * from spring.t_teacher where id = ?";
        Object[] params = {102};
        Teacher teacher = null;
        try {
            teacher = jdbcBaseDao.queryOne(sql, params, Teacher.class);
        } catch (Exception e) {
            log.error("查询教师详情失败",e);
        }
        return CommonResponse.success(teacher);
    }

    @RequestMapping("/template/queryOne")
    public CommonResponse templateQueryOne(){
        String sql = "select * from spring.t_teacher where id = ?";
        Object[] params = {101};
        Teacher teacher = templateBaseDao.templateQueryOne(sql, params, Teacher.class);
        return CommonResponse.success(teacher);
    }

    @RequestMapping("/mybatis/queryOne")
    public CommonResponse mybatisQueryOne(){
        Teacher teacher = teacherMapper.findById(100);
        return CommonResponse.success(teacher);
    }

    @RequestMapping("/mybatis/selectOne")
    public CommonResponse selectOne(){
        Teacher teacher = teacherMapper.selectOne(101);
        return CommonResponse.success(teacher);
    }

    @RequestMapping("/jdbc/all")
    public CommonResponse GetAll(){
        String sql = "select * from spring.t_teacher";
        Object[] params = null;
        List<Teacher> list = null;
        try {
            list = jdbcBaseDao.queryList(sql, null, Teacher.class);
        } catch (Exception e) {
            log.error("查询教师列表失败",e);
        }
        return CommonResponse.success(list);
    }

    @RequestMapping("/jdbcDruid/all")
    public CommonResponse jdbcDruidGetAll(){
        String sql = "select * from spring.t_teacher";
        Object[] params = null;
        List<Teacher> list = null;
        try {
            list = jdbcBaseDao.queryList(sql, null, Teacher.class);
        } catch (Exception e) {
            log.error("查询教师列表失败",e);
        }
        return CommonResponse.success(list);
    }

    @RequestMapping("/template/all")
    public CommonResponse templateGetAll(){
        String sql = "select * from spring.t_teacher";
        List list = null;
        try {
            list = templateBaseDao.templateQuery(sql, null);
        } catch (Exception e) {
            log.error("查询教师列表失败",e);
        }
        return CommonResponse.success(list);
    }

    @RequestMapping("/mybatis/all")
    public CommonResponse mybatisGetAll(){
        List<Teacher> list = sqlSession.selectList("com.ruyuan.little.project.spring.mapper.TeacherMapper.selectAll");
        return CommonResponse.success(list);
    }

    @RequestMapping("/mybatis/selectName")
    public CommonResponse selectOne(String name){
        List<Teacher> list = teacherMapper.findByName(name);
        return CommonResponse.success(list);
    }
}
