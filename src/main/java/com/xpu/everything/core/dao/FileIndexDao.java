package com.xpu.everything.core.dao;

import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;

import java.util.List;

/**
 * 关于业务层访问数据库的CRUD
 */
public interface FileIndexDao {
    /**
     * 插入数据 Thing
     * @param thing 需要插入的数据
     */
    void insert(Thing thing);

    /**
     * 根据condition的条件进行数据库的检索
     * @param condition 检索条件
     * @return 检索结果
     */
    List<Thing> search(Condition condition);
}
