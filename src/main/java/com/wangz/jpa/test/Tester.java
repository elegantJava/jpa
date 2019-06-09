package com.wangz.jpa.test;

import com.alibaba.druid.support.json.JSONUtils;
import com.wangz.jpa.common.Criteria;
import com.wangz.jpa.common.Restrictions;
import com.wangz.jpa.model.Student;
import com.wangz.jpa.repository.StudentRepository;
import com.wangz.jpa.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName Tester
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/9 18:06
 * @Description: TODO
 */
@RestController()
@RequestMapping("/test")
public class Tester {

    @Autowired
    private StudentServiceImpl service;
    @GetMapping
    public String test(){
        return service.test().toString();
    }
}
