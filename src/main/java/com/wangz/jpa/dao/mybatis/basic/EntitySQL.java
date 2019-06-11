package com.wangz.jpa.dao.mybatis.basic;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntitySQL {

    private Class<?> entityClass;

    private EntitySQL(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public static EntitySQL from(Class<?> obj) {
        return new EntitySQL(obj);
    }

    /**
     * 获取所有的父类,包括父类的父类
     */
    @SuppressWarnings("rawtypes")
    private List<Class> getSuperclasses(Class<?> class1) {
        List<Class> ret = new ArrayList<Class>();
        Class superClass = class1.getSuperclass();
        if (superClass != null) {
            ret.add(superClass);
            ret.addAll(getSuperclasses(superClass));
        }
        return ret;
    }

    /**
     * 获取类(包括父类,父类的父类)的Field
     */
    private List<Field> getAllDeclaredFields(Class class1) {

        List<Class> classes = new ArrayList<Class>();
        classes.add(class1);
        classes.addAll(getSuperclasses(class1));

        List<Field> ret = new ArrayList<Field>();
        for (Class class2 : classes) {
            Field[] fields = class2.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    ret.add(field);
                }
            }
        }
        return ret;

    }

    private Field findField(List<Field> fields, String name) {
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    private List<String> getPersistPropertyNames() {

        try {

            List<Field> fields = getAllDeclaredFields(this.entityClass);

            List<String> propertyNames = new ArrayList<String>();
            BeanInfo beanInfo = Introspector.getBeanInfo(this.entityClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null) {
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    String name = propertyDescriptor.getName();
                    if ("class".equals(name)) {
                        continue;
                    }

                    if (propertyDescriptor.getReadMethod() != null &&
                            propertyDescriptor.getReadMethod().getAnnotation(IgnoreField.class) != null) {
                        continue;
                    }

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
            throw new RuntimeException("获取" + this.entityClass + "的反射信息失败", e);
        }
    }

    public String deleteSQL() {
        String tablename = this.tableName();
        String[] idname = this.idName();
        String condition = whereConditionByIdName(idname);
        return "delete from " + tablename + " where " + condition;
    }

    private String whereConditionByIdName(String[] idnames) {
        return whereConditionByIdNameWithParam(idnames, "");
    }

    private String whereConditionByIdNameWithParam(String[] idnames, String paramPrefix) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String idname : idnames) {
            if (!first) {
                builder.append(" and ");
            }
            builder.append(upperCamelToLowerUnderscore(idname) + "=#{" + paramPrefix + idname + "}");
        }
        return builder.toString();
    }

    public String getSQL() {
        String tablename = this.tableName();
        String[] idname = this.idName();
        String condition = whereConditionByIdName(idname);
        String getsql = "select * from " + tablename + " where " + condition;
        return getsql;
    }

    public String updateByAnnotationSQL(Class<? extends Annotation> group) {
        return updateByAnnotationSQL(group, "");
    }

    public String updateByAnnotationSQL(Class<? extends Annotation> group, String paramPrefix) {
        try {
            String tableName = this.tableName();
            List<String> propertyNames = getUpdatePartPropertyNames(group);

            String[] primaryKeys = idName();
            for (String primaryKey : primaryKeys) {
                propertyNames.remove(primaryKey);
            }

            List<String> updatePropertySqls = new ArrayList<String>(propertyNames.size());
            for (String propertyName : propertyNames) {
                updatePropertySqls.add(upperCamelToLowerUnderscore(propertyName) + "=#{" + paramPrefix + propertyName + "}");
            }

            String fields = StringUtils.seperate(updatePropertySqls, ",");

            String condition = whereConditionByIdNameWithParam(primaryKeys, paramPrefix);

            String sql = "update " + tableName +
                    " set " + fields +
                    " where " + condition;

            return sql;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getUpdatePartPropertyNames(Class<? extends Annotation> group) throws IntrospectionException {
        List<Field> fields = getAllDeclaredFields(this.entityClass);

        String[] primaryKeys = idName();

        List<String> propertyNames = new ArrayList<String>();
        BeanInfo beanInfo = Introspector.getBeanInfo(this.entityClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                if ("class".equals(name)) {
                    continue;
                }

                Field field = findField(fields, name);

                if (ArrayUtils.contains(primaryKeys, name)
                        || (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().getAnnotation(group) != null)
                        || (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getWriteMethod().getAnnotation(group) != null)
                        || (field != null && field.getAnnotation(group) != null)) {
                    propertyNames.add(name);
                }

            }
        }
        return propertyNames;
    }

    public String insertSQL() {

        List<String> propertyNames = getPersistPropertyNames();
        List<String> tableColumeNames = upperCamelToLowerUnderscore(propertyNames);

        String tablename = this.tableName();

        String insertSql = "insert into " + tablename + "(" +
                StringUtils.seperate(tableColumeNames, ",") +
                ") values (" +
                StringUtils.seperate(propertyNames, ",", "#{", "}") +
                ")";

        return insertSql;
    }

    private List<String> upperCamelToLowerUnderscore(List<String> propertyNames) {
        List<String> result = new ArrayList<String>(propertyNames.size());
        for (String propertyName : propertyNames) {
            result.add(upperCamelToLowerUnderscore(propertyName));
        }
        return result;
    }

    private String upperCamelToLowerUnderscore(String propertyName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName);
    }

    private String[] idName() {
        Table table = this.entityClass.getAnnotation(Table.class);
        if (table != null) {
            return table.primaryKey().split(",");
        }

        return new String[]{"id"};
    }

    private String tableName() {
        Table table = this.entityClass.getAnnotation(Table.class);
        if (table != null) {
            if (notEmpty(table.tableName())) {
                return table.tableName();
            }
        }
        return this.entityClass.getSimpleName();
    }

    private boolean notEmpty(String s) {
        return s != null && !s.isEmpty();
    }

}
