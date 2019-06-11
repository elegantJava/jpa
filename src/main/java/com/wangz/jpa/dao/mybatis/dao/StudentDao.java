package com.wangz.jpa.dao.mybatis.dao;

import com.wangz.jpa.dao.mybatis.basic.BaseDao;
import com.wangz.jpa.model.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentDao  extends BaseDao<Student>  {

}
