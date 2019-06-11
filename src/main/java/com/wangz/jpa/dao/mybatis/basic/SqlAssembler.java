package com.wangz.jpa.dao.mybatis.basic;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Table;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName SqlAssembler
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 12:07
 * @Description: TODO 组装sql
 */
public class SqlAssembler {

    private Class<?> clazz;

    private SqlAssembler(Class<?> clazz){
        this.clazz = clazz;
    }

    public static SqlAssembler from(Class<?> clazz) {
        return new SqlAssembler(clazz);
    }

    /**
     * 递归获取所有的父类集合 --- 考虑到实体类继承抽象超父类等
     */
    @SuppressWarnings("rawtypes")
    private Set<Class> getSuperclasses(Class<?> clazz) {
        List<Class> classList = new ArrayList<>();
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            classList.add(superClass);
            classList.addAll(getSuperclasses(superClass));
        }
        return new HashSet<>(classList);
    }

    /**
     * 获取类的Field集合（包含递归父类）
     */
    private Set<Field> getAllFields(Class clazz) {
        // 存放当前类与其所有超类集合
        List<Class> classes = new ArrayList<>();
        classes.add(clazz);// 自身类
        classes.addAll(this.getSuperclasses(clazz));

        // 提取 classes 中所有声明字段
        List<Field> fieldList = new ArrayList<>();
        for (Class c : classes) {
            Field[] fields = c.getDeclaredFields();
            Arrays.asList(fields).forEach(field -> {
                field.setAccessible(true);
                if ("serialVersionUID" .equals(field.getName()))
                    return;
                fieldList.add(field);
            });
        }
        return new HashSet<>(fieldList);
    }

    private Field findField(Set<Field> fields, String name) {
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }
    /**
     * @return 内省 类所有信息
     */
    private List<String> getPersistPropertyNames() {
        try {
            Set<Field> fields = getAllFields(this.clazz);

            List<String> propertyNames = new ArrayList<>();

            BeanInfo beanInfo = Introspector.getBeanInfo(this.clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            if (propertyDescriptors != null) {
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    String name = propertyDescriptor.getName();
                    if ("class".equals(name)) {
                        continue;
                    }

                    // getter
                    if (propertyDescriptor.getReadMethod() != null &&
                            propertyDescriptor.getReadMethod().getAnnotation(IgnoreField.class) != null) {
                        continue;
                    }

                    // setter
                    if (propertyDescriptor.getWriteMethod() != null &&
                            propertyDescriptor.getWriteMethod().getAnnotation(IgnoreField.class) != null) {
                        continue;
                    }

                    Field field = findField(fields, name);
                    if (field != null && field.getAnnotation(IgnoreField.class) != null) {
                        continue;
                    }

                    propertyNames.add(name);
                }
            }
            return propertyNames;

        } catch (IntrospectionException e) {
            throw new RuntimeException("Failed to obtain " + this.clazz + " reflection information!" , e);
        }
    }


    /**
     * @return @Table 中获取 name 信息 , 若没有该注解，则默认取该类的 simpleName
     */
    private String tableName() {
        Table table = this.clazz.getAnnotation(Table.class);
        if (table != null) {
            if(StringUtils.isNotBlank(table.name()))
                return table.name();
        }
        return this.clazz.getSimpleName();
    }

    /**
     * 对表名及表中字段名的驼峰命名风格 统一转换成 数据库的小写字母+下划线风格
     * @param propertyName propertyName
     * @return  propertyName -> property_name
     */
    private String upperCamelToLowerUnderscore(String propertyName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName);
    }

    // 与上反之
    private String lowerUnderscoreToUpperCamel(String propertyName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, propertyName);
    }

    //======================================================================
    //                          开始组装sql
    //======================================================================


    public String queryByPrimaryKeySQL() {
        SQL sql = new SQL();

        // select ...
        List<String> persistPropertyNames = getPersistPropertyNames();
        persistPropertyNames.forEach(col->{
            String name = String.format("%s", col);
            sql.SELECT(upperCamelToLowerUnderscore(name));
        });

        // from tableName
        String tableName = this.tableName();
        sql.FROM(tableName);

        // where id = #{id}
        sql.WHERE("1=1").AND();
        sql.WHERE("id = #{id}");
        return sql.toString();
    }


    public String insertSQL() {
        SQL sql = new SQL();

        String tableName = this.tableName();
        sql.INSERT_INTO(tableName);

        List<String> persistPropertyNames = getPersistPropertyNames();
        persistPropertyNames.forEach(col->{
            String name = String.format("%s", col);
            if (name.equals("id")) return;
            sql.VALUES(upperCamelToLowerUnderscore(name), String.format("#{%s}", col));
        });

        return sql.toString();
    }

    public String updateSQL() {
        SQL sql = new SQL();
        String tableName = this.tableName();
        sql.UPDATE(tableName);
        List<String> persistPropertyNames = getPersistPropertyNames();
        persistPropertyNames.forEach(col->{
            String name = String.format("%s", col);
            if (name.equals("id")) return;
            sql.SET(String.format("%s = #{%s}", upperCamelToLowerUnderscore(name), col));
        });

        sql.WHERE("1=1").AND().WHERE("id = #{id}");

        return sql.toString();
    }

    public String deleteByPrimaryKeySQL() {
        SQL sql = new SQL();
        String tableName = this.tableName();
        sql.DELETE_FROM(tableName)
                .WHERE("1=1")
                .AND()
                .WHERE("id = #{id}");
        return sql.toString();
    }

}
