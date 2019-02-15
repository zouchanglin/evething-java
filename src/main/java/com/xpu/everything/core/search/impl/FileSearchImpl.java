package com.xpu.everything.core.search.impl;

import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;
import com.xpu.everything.core.search.FileSearch;

import java.util.ArrayList;
import java.util.List;

public class FileSearchImpl implements FileSearch {
    private final FileIndexDao fileIndexDao;

    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }


    @Override
    public List<Thing> search(Condition condition) {
        return fileIndexDao.search(condition);
    }
}
