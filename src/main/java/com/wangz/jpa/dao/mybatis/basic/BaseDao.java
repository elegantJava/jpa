package com.wangz.jpa.dao.mybatis.basic;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public interface BaseDao<T> {

    String primaryKey ="id";

    @SelectProvider(type = BaseDaoProvider.class, method = "queryByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    T queryByPrimaryKey(Serializable id);

    @InsertProvider(type = BaseDaoProvider.class, method = "insert")
    T insert(T entity);


    @UpdateProvider(type = BaseDaoProvider.class, method = "updateByPrimaryKey")
    @Options(useGeneratedKeys = true, keyColumn = primaryKey)
    void updateByPrimaryKey(Serializable id);

    @UpdateProvider(type = BaseDaoProvider.class, method = "updateByAnnotation")
    void updateByAnnotation(T entity, Class<? extends Annotation> updateAnnotation);

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

        public String updateByPrimaryKey(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).updateByPrimaryKeySQL();
        }

        public String deleteByPrimaryKey(ProviderContext providerContext) {
            Class entityClass = this.getMapperType(providerContext);
            return SqlAssembler.from(entityClass).deleteByPrimaryKeySQL();
        }


    }

}