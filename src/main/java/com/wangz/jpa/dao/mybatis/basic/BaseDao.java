package com.wangz.jpa.dao.mybatis.basic;

import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * 提供单表通用操作
 *
 * @param <T>
 */
public interface BaseDao<T> {

    String primaryKey ="id";

    //======================================================================
    //                          查询
    //======================================================================

    //                          按id查询
    //----------------------------------------------------------------------
    @SelectProvider(type = BaseDaoProvider.class, method = "queryByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    T queryByPrimaryKey(Serializable id);

    @SelectProvider(type = BaseDaoProvider.class, method = "queryByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    T findById(Serializable id);


    //                          查询列表
    //----------------------------------------------------------------------
//    @SelectProvider(type = BaseDaoProvider.class, method = "list")
//    Collection<T> list();


    //======================================================================
    //                          新增
    //======================================================================

    @InsertProvider(type = BaseDaoProvider.class, method = "insert")
    void insert(T entity);

    //======================================================================
    //                          更新
    //======================================================================

    @UpdateProvider(type = BaseDaoProvider.class, method = "update")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    void update(T entity);


    //======================================================================
    //                          物理删除
    //======================================================================

    @DeleteProvider(type = BaseDaoProvider.class, method = "deleteByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    void deleteByPrimaryKey(Serializable id);


}
