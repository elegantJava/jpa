package com.wangz.jpa.dao.mybatis.basic;

import tk.mybatis.mapper.common.*;

public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> ,  ConditionMapper<T>, IdsMapper<T>{

}