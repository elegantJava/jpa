package com.wangz.jpa.dao.mybatis.basic;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public interface BaseDao<T> {

    String primaryKey ="id";

    @SelectProvider(type = BaseDaoProvider.class, method = "queryByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    T queryByPrimaryKey(Serializable id);

    @SelectProvider(type = BaseDaoProvider.class, method = "queryByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    T findById(Serializable id);


    @InsertProvider(type = BaseDaoProvider.class, method = "insert")
    void insert(T entity);

    @UpdateProvider(type = BaseDaoProvider.class, method = "update")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    void update(T entity);

    @DeleteProvider(type = BaseDaoProvider.class, method = "deleteByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    void deleteByPrimaryKey(Serializable id);


    //======================================================================
    //                          SQLProvider
    //======================================================================

    class BaseDaoProvider {

        private String getDatabaseId(ProviderContext providerContext){
            return providerContext.getDatabaseId();
        }

        /** 获取 T 的原型*/
        private Class getMapperType(ProviderContext providerContext) {
            Class mapperType = providerContext.getMapperType();
            ParameterizedType genericSuperclass = (ParameterizedType)mapperType.getGenericInterfaces()[0];
            return (Class) genericSuperclass.getActualTypeArguments()[0];
        }

        /**隐式调用以下方法*/
        public String queryByPrimaryKey(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).queryByPrimaryKeySQL();
        }

        public String insert(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).insertSQL();
        }

        public String update(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).updateSQL();
        }

        public String deleteByPrimaryKey(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).deleteByPrimaryKeySQL();
        }


    }

}
