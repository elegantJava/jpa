package com.wangz.jpa.dao.basemapper.Service;

import com.wangz.jpa.dao.mybatis.dao.UserDao;
import com.wangz.jpa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user = new User();
        user.setName("name_add");
        user.setAge(11);
        userDao.insert(user);
        user.setName("name_up");
        user.setId(11);
        userDao.updateByPrimaryKey(user);

        List<User> all = userDao.selectAll();
        System.out.println(all.size());

//        // 创建Example
//        Example example = new Example(User.class);
//// 创建Criteria
//        Example.Criteria criteria = example.createCriteria();
//// 添加条件
//        criteria.andEqualTo("id", 2);
//        List<User> list = userDao.selectByExample(example);
        return "ok";

    }
}
