package com.wangz.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wangz.jpa.dao.mybatis.basic.IgnoreField;
import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName Student
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/9 17:46
 * @Description: TODO
 */
@Data
@Table(name="student")
@Entity
public class Student {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String gender;
    @JsonIgnore
    private String password;
    private Integer age;
}
