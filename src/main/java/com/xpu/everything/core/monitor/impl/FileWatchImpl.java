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



//import com.xpu.everything.core.common.HandlePath;
//import com.xpu.everything.core.dao.FileIndexDao;
//import com.xpu.everything.core.monitor.FileWatch;
//import org.apache.commons.io.monitor.FileAlterationListener;
//import org.apache.commons.io.monitor.FileAlterationMonitor;
//import org.apache.commons.io.monitor.FileAlterationObserver;
//
//import java.io.File;
//
//public class FileWatchImpl implements FileWatch,
//        FileAlterationListener {
//    private FileIndexDao fileIndexDao;
//
//    public FileWatchImpl(FileIndexDao fileIndexDao) {
//        this.fileIndexDao = fileIndexDao;
//    }

//    @Override
//    public void onStart(FileAlterationObserver fileAlterationObserver) {
//        //fileAlterationObserver.addListener(this);
//    }
//
//    @Override
//    public void onDirectoryCreate(File file) {
//
//    }
//
//    @Override
//    public void onDirectoryChange(File file) {
//
//    }
//
//    @Override
//    public void onDirectoryDelete(File file) {
//        System.out.println("onDirectoryDelete"+file.getAbsolutePath());
//    }
//
//    @Override
//    public void onFileCreate(File file) {
//        //文件创建
//        System.out.println("new File Create "+ file.getAbsolutePath());
//        this.fileIndexDao.insert(FileConverThing.convert(file));
//    }
//
//    @Override
//    public void onFileChange(File file) {
//        System.out.println("onFileChange" + file.getAbsolutePath());
//    }
//
//    @Override
//    public void onFileDelete(File file) {
//        System.out.println("onFileDelete" + file.getAbsolutePath());
//        this.fileIndexDao.delete(FileConverThing.convert(file));
//    }
//
//    @Override
//    public void onStop(FileAlterationObserver fileAlterationObserver) {
//        //移除监听
//        fileAlterationObserver.removeListener(this);
//    }


//    //---------------------
//    @Override
//    public void start() {
//        try {
//            this.monitor.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void monitor(HandlePath handlePath) {
//        //监控的是includePath集合
//        for(String path:handlePath.getIncludePath()){
//            FileAlterationObserver observer = new FileAlterationObserver(path,
//                    pathname->{
//                String currentPath = pathname.getAbsolutePath();
//                for(String excludePath: handlePath.getExcludePath()){
//                    if(excludePath.startsWith(currentPath)){
//                        return false;
//                    }
//                }
//                return true;
//            });
//            observer.addListener(this);
//            monitor.addObserver(observer);
//        }
//        //this.monitor.addObserver();
//    }
//
//    @Override
//    public void stop() {
//        try {
//            this.monitor.stop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //-----------------------------------------------
//    private String rootpath = "";
//    public FileWatchImpl(String rootpath) {
//        this.rootpath = rootpath;
//    }
//    @Override
//    public void onStart(FileAlterationObserver fileAlterationObserver) {
//
//    }
//
//    @Override
//    public void onDirectoryCreate(File file) {
//
//    }
//
//    @Override
//    public void onDirectoryChange(File file) {
//
//    }
//
//    @Override
//    public void onDirectoryDelete(File file) {
//
//    }
//
//    @Override
//    public void onFileCreate(File file) {
//        System.out.println("onFileCreate " + file.getAbsolutePath());
//    }
//
//    @Override
//    public void onFileChange(File file) {
//        System.out.println("onFileChange " + file.getAbsolutePath());
//    }
//
//    @Override
//    public void onFileDelete(File file) {
//        System.out.println("onFileDelete " + file.getAbsolutePath());
//    }
//
//    @Override
//    public void onStop(FileAlterationObserver fileAlterationObserver) {
//
//    }
//}
