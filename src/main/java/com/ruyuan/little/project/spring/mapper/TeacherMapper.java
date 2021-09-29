package com.ruyuan.little.project.spring.mapper;

import com.ruyuan.little.project.spring.dto.Teacher;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:教师mapper组件
 **/
public interface TeacherMapper {

    /**
     * 根据教师id获取教师详情
     *
     * @param id 教师id
     * @return 教师详情
     */
    @Select("select * from t_teacher where id = #{id}")
    Teacher findById(int id);

    /**
     * 根据教师id获取教师详情
     *
     * @param id 教师id
     * @return 教师详情
     */
    Teacher selectOne(int id);

    /**
     * 根据教师名称查询教师
     *
     * @param name 教师名称
     * @return 教师列表
     */
    List<Teacher> findByName(String name);


    /**
     * 查询教师列表
     *
     * @param teacher 查询条件
     * @return 教师列表
     */
    List<Teacher> getPage(Teacher teacher);

    /**
     * 所有教师列表
     *
     * @return 返回结果
     */
    List<Teacher> selectAll();

    /**
     * 修改教师信息
     *
     * @param teacherList 教师列表
     * @return 操作结果
     */
    int updateTeacherList(List<Teacher> teacherList);

    /**
     * 修改教师信息
     *
     * @param teacher 教师信息
     * @return 操作结果
     */
    int updateTeachingDays(Teacher teacher);

    /**
     * 查询教师列表总数
     *
     * @param teacher 教师信息
     * @return 查询结果
     */
    int getPageTotal(Teacher teacher);
}
