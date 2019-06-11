package com.wangz.jpa.dao.mybatis.Service;

import com.wangz.jpa.dao.mybatis.dao.UserDao;
import com.wangz.jpa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName UserService
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 16:38
 * @Description: TODO
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String select(){
        // 创建Example
        Example example = new Example(User.class);
// 创建Criteria
        Example.Criteria criteria = example.createCriteria();
// 添加条件
        criteria.andEqualTo("id", 2);
        List<User> list = userDao.selectByExample(example);
        User user = list.get(0);
        return user.getName();
    }
}
