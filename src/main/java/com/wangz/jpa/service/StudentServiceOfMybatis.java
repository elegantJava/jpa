package com.wangz.jpa.service;

import com.wangz.jpa.dao.mybatis.basic.UpdateTool;
import com.wangz.jpa.dao.mybatis.dao.StudentDao;
import com.wangz.jpa.model.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName StudentServiceOfMybatis
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 11:10
 * @Description: TODO
 */
@Service
public class StudentServiceOfMybatis {

    private final StudentDao studentDao;

    @Autowired
    public StudentServiceOfMybatis(StudentDao studentDao){
        this.studentDao = studentDao;
    }

    public String queryByPrimaryKey(Integer id){
        Student insert = studentDao.queryByPrimaryKey(id);
        return insert.getName();
    }
    @Transactional(rollbackFor = Exception.class)
    public String insert(){
        Student student = new Student();
        student.setAge(15);
        student.setName("wang1");
        studentDao.insert(student);
        return student.getId().toString();
    }
    @Transactional(rollbackFor = Exception.class)
    public String update(){
        Student student = new Student();
        student.setAge(123);
        student.setName("test_update");
        Student byId = studentDao.findById(2);
        UpdateTool.copyNullProperties(byId,student);
        studentDao.update(student);
        return "up ok";
    }
}
