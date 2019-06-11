package com.wangz.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName User
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 16:36
 * @Description: TODO
 */
@Data
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String gender;
    @JsonIgnore
    private String password;
    private Integer age;
}
