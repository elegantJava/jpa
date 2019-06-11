package com.wangz.jpa.dao.basemapper.basic;

import tk.mybatis.mapper.common.*;

public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> ,  ConditionMapper<T>, IdsMapper<T>{

}