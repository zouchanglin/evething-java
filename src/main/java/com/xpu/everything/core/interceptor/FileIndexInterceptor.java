package com.xpu.everything.core.interceptor;

import com.xpu.everything.core.common.FileConverThing;
import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.model.Thing;

import java.io.File;

/**
 * 文件插入的拦截器，实现FileInterceptor接口
 */
public class FileIndexInterceptor implements FileInterceptor {
    /**
     * 文件操作的Dao层对象
     */
    private FileIndexDao fileIndexDao;

    /**
     * 构造方法传入Dao层操作对象
     * @param fileIndexDao Dao层操作对象：Dao层插入对象
     */
    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    /**
     * 插入数据库表记录的实现方法
     * @param file 要插入的文件（已经过滤）
     */
    @Override
    public void apply(File file) {
        Thing thing = FileConverThing.convert(file);
        fileIndexDao.insert(thing);
    }
}
