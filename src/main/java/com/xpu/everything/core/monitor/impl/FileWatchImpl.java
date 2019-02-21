package com.xpu.everything.core.monitor.impl;


import com.xpu.everything.config.PropertiesParse;
import com.xpu.everything.core.common.FileConverThing;
import com.xpu.everything.core.common.HandlePath;
import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.monitor.FileWatch;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class FileWatchImpl implements FileAlterationListener, FileWatch {
    private FileAlterationMonitor monitor =
            new FileAlterationMonitor(PropertiesParse.getIntervalTime());

    public FileAlterationMonitor getFileAlterationMonitor(){
        return monitor;
    }
    private FileIndexDao fileIndexDao;

    public FileWatchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void onDirectoryCreate(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryCreate " + file.getAbsolutePath());
    }

    @Override
    public void onDirectoryChange(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryChange " + file.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryDelete " + file.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileCreate " + file.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(file));
    }

    @Override
    public void onFileChange(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileChange " + file.getAbsolutePath());
    }

    @Override
    public void onFileDelete(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileDelete " + file.getAbsolutePath());
        this.fileIndexDao.delete(FileConverThing.convert(file));
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void monitor(HandlePath handlePath) {
        FileWatchImpl.MyFileFilter s = new MyFileFilter(handlePath);
        for(String root:handlePath.getIncludePath()){
            FileAlterationObserver observer = new FileAlterationObserver(root, s);
            observer.addListener(this);
            monitor.addObserver(observer);
        }
    }

    class MyFileFilter implements FileFilter {
        private final HandlePath handlePath;

        MyFileFilter(HandlePath handlePath) {
            this.handlePath = handlePath;
        }

        @Override
        public boolean accept(File file) {
            Set<String> excludePath = handlePath.getExcludePath();
            for(String exclude:excludePath)
                if (file.getAbsolutePath().startsWith(exclude)) return false;
            return true;
        }
    }
}