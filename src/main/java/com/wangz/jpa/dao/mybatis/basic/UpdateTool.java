package com.wangz.jpa.dao.mybatis.basic;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName UpdateTool
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 15:26
 * @Description: TODO 更新复制
 */
public class UpdateTool {
    /**
     *  例如更新user1时，user1在数据库的记录有多个null字段，
     *  将这些字段安全地复制到用作新的更新实体 user_update 中
     *  忽略user1中不为空的字段 ：ignoreProperties = getNoNullProperties(target)
     *
     * @param source 待提交更新的对象
     * @param target 数据库中等待被操作的对象
     */
    public static void copyNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNoNullProperties(target));
    }

    /**
     * @param target 目标源数据
     * @return 提取目标源中不为空的字段
     */
    private static String[] getNoNullProperties(Object target) {
        BeanWrapper srcBean = new BeanWrapperImpl(target);
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        Set<String> noEmptyName = new HashSet<>();
        for (PropertyDescriptor p : pds) {
            Object value = srcBean.getPropertyValue(p.getName());
            if (value != null) noEmptyName.add(p.getName());
        }
        String[] result = new String[noEmptyName.size()];
        return noEmptyName.toArray(result);
    }
}
