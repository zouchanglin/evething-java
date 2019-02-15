package com.xpu.everything.core.search;

import com.xpu.everything.core.dao.DataSourceFactory;
import com.xpu.everything.core.dao.impl.FileIndexImpl;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;
import com.xpu.everything.core.search.impl.FileSearchImpl;

import java.util.List;

/**
 * 检索的接口
 */
public interface FileSearch {
    List<Thing> search(Condition condition);
}
