package com.xpu.everything.core.interceptor;

import com.xpu.everything.core.common.FileConverThing;
import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.model.Thing;

import java.io.File;

public class FileIndexInterceptor implements FileInterceptor {
    private FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        Thing thing = FileConverThing.convert(file);
        fileIndexDao.insert(thing);
    }
}
