package com.wangz.jpa.service;

import com.alibaba.fastjson.JSON;
import com.wangz.jpa.dao.springdata.basic.Criteria;
import com.wangz.jpa.dao.springdata.basic.Restrictions;
import com.wangz.jpa.dao.springdata.repository.StudentRepository;
import com.wangz.jpa.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName StudentServiceImpl
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/9 18:25
 * @Description: TODO
 */
@Service
public class StudentServiceImpl {

    private final StudentRepository repository;
    @Autowired
    private StudentServiceImpl(StudentRepository repository){
        this.repository = repository;
    }

    /**
     * spring data
     * @return
     */
    public Object test(){
        Criteria<Student> criteria = new Criteria<>();
        criteria
                .add(Restrictions.eq("name","test"))
                .add(Restrictions.gt("age",16));

        List<Student> list = repository.findAll(criteria);
        return JSON.toJSON(list);
    }

}
