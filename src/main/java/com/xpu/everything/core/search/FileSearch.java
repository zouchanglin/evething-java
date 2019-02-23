package com.xpu.everything.core.search;

import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;

import java.util.List;

/**
 * 检索的接口
 */
public interface FileSearch {
    /**
     * 根据传入的条件进行搜索
     * @param condition 传入的搜索条件
     * @return 返回搜索结果:List<thing>
     */
    List<Thing> search(Condition condition);
}
