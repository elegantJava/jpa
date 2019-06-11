package com.wangz.jpa.dao.mybatis.basic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String tableName() default "";

    String primaryKey() default "id";
}
