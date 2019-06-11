package com.wangz.jpa.controller;

import com.wangz.jpa.model.Student;
import com.wangz.jpa.service.StudentServiceImpl;
import com.wangz.jpa.service.StudentServiceOfMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName Tester
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/9 18:06
 * @Description: TODO
 */
@RestController()
@RequestMapping("/test")
public class TestController {

    private final StudentServiceImpl service;
    private final StudentServiceOfMybatis serviceOfMybatis;
    @Autowired
    public TestController(StudentServiceImpl service,StudentServiceOfMybatis serviceOfMybatis){
        this.service = service;
        this.serviceOfMybatis = serviceOfMybatis;
    }
    @GetMapping
    public String test(){
        return service.test().toString();
    }

    @GetMapping("/test1")
    public String queryByPrimaryKey(Integer id){
        return serviceOfMybatis.queryByPrimaryKey(id);
    }

    @GetMapping("/test2")
    public String insert(){
        return serviceOfMybatis.insert();
    }
    @GetMapping("/test3")
    public String update(){
        return serviceOfMybatis.update();

    }

}
