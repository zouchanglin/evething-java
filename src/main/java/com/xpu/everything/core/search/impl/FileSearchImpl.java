package com.xpu.everything.core.search.impl;

import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;
import com.xpu.everything.core.search.FileSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件检索的实现类，实现了FileSearch接口
 * 通过Dao层对象获取搜索结果
 */
public class FileSearchImpl implements FileSearch {
    /**
     * 需要操作数据的Dao层对象
     */
    private final FileIndexDao fileIndexDao;

    /**
     * 通过构造方法传入Dao层对象
     * @param fileIndexDao 需要参与构造的Dao层对象
     */
    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }


    /**
     * 实现search接口方法，根据传入的条件进行搜索
     * @param condition 传入的搜索条件
     * @return 返回搜索结果:List<thing>
     */
    @Override
    public List<Thing> search(Condition condition) {
        if(condition == null){
            return new ArrayList<>();
        }
        return fileIndexDao.search(condition);
    }
}
