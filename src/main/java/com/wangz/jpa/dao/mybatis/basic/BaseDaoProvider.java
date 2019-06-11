package com.wangz.jpa.dao.mybatis.basic;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.ParameterizedType;

/**
 * @ClassName BaseDaoProvider
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/11 15:59
 * @Description: TODO
 */
public class BaseDaoProvider {

    /**隐式调用以下方法*/

    //======================================================================
    //                          查询
    //======================================================================
    public String queryByPrimaryKey(ProviderContext providerContext) {
        Class entityClass = this.getMapperType(providerContext);
        return SqlAssembler.from(entityClass).queryByPrimaryKeySQL();
    }

//    public String list(ProviderContext providerContext) {
//        Class entityClass = this.getMapperType(providerContext);
//        return SqlAssembler.from(entityClass).listSQL();
//    }










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



    /** 获取 T 的原型*/
    private Class getMapperType(ProviderContext providerContext) {
        Class mapperType = providerContext.getMapperType();
        ParameterizedType genericSuperclass = (ParameterizedType)mapperType.getGenericInterfaces()[0];
        return (Class) genericSuperclass.getActualTypeArguments()[0];
    }
}
