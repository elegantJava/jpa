package com.wangz.jpa.service;

import com.wangz.jpa.dao.mybatis.dao.StudentDao;
import com.wangz.jpa.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private StudentServiceOfMybatis(StudentDao studentDao){
        this.studentDao = studentDao;
    }

    public String testGet(){
        Student student = new Student();
        student.setAge(11);
        student.setName("wang");
        student.setId(5);
        Student insert = studentDao.insert(student);
        return insert.getId().toString();
    }
}
