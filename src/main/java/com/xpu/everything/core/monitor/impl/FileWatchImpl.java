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

/**
 * 文件系统检测实现类，实现了FileWatch，FileAlterationListener
 */
public class FileWatchImpl implements FileAlterationListener, FileWatch {
    /**
     * 文件系统监听器实例对象，监听间隔时间来自于配置文件解析器
     */
    private FileAlterationMonitor monitor =
            new FileAlterationMonitor(PropertiesParse.getIntervalTime());

    /**
     * 实现了FileWatch中的获取监听器实例方法：getFileAlterationMonitor
     * @return 返回监听器的实例对象
     */
    public FileAlterationMonitor getFileAlterationMonitor(){
        return monitor;
    }

    /**
     * Dao层操作实例，负责插入操作
     */
    private FileIndexDao fileIndexDao;

    /**
     * 通过构造方法引入Dao层操作实例
     * @param fileIndexDao Dao层操作实例
     */
    public FileWatchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    /**
     * 文件系统监听开始的回调函数，看需求实现
     * @param fileAlterationObserver 文件变更观察者
     */
    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }

    /**
     * 监听文件夹的创建的回调函数
     * @param file 监听到的创建的文件夹
     */
    @Override
    public void onDirectoryCreate(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryCreate " + file.getAbsolutePath());
    }

    /**
     * 监听文件夹的修改的回调函数
     * @param file 监听到的修改的文件夹
     */
    @Override
    public void onDirectoryChange(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryChange " + file.getAbsolutePath());
    }

    /**
     * 监听文件夹的删除的回调函数
     * @param file 监听到的删除的文件夹
     */
    @Override
    public void onDirectoryDelete(File file) {
        //TODO 监视结果可见
        //System.out.println("onDirectoryDelete " + file.getAbsolutePath());
    }

    /**
     * 监听文件的创建的回调函数
     * @param file 监听到的创建的文件
     */
    @Override
    public void onFileCreate(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileCreate " + file.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(file));
    }

    /**
     * 监听文件的修改的回调函数
     * @param file 监听到的修改的文件
     */
    @Override
    public void onFileChange(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileChange " + file.getAbsolutePath());
    }

    /**
     * 监听文件的删除的回调函数
     * @param file 监听到的删除的文件
     */
    @Override
    public void onFileDelete(File file) {
        //TODO 监视结果可见
        //System.out.println("onFileDelete " + file.getAbsolutePath());
        this.fileIndexDao.delete(FileConverThing.convert(file));
    }

    /**
     * 文件监视系统停止的回调函数
     * @param fileAlterationObserver 文件变更观察者
     */
    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }

    /**
     *  实现FileAlterationListener接口需要实现的监视方法monitor
     * @param handlePath 处理路径
     */
    @Override
    public void monitor(HandlePath handlePath) {
        FileWatchImpl.MyFileFilter s = new MyFileFilter(handlePath);
        for(String root:handlePath.getIncludePath()){
            FileAlterationObserver observer = new FileAlterationObserver(root, s);
            observer.addListener(this);
            monitor.addObserver(observer);
        }
    }

    /**
     * 自定义内部类过滤器实现监听过滤接口
     */
    class MyFileFilter implements FileFilter {
        /**
         * 过滤器处理路径
         */
        private final HandlePath handlePath;

        /**
         * 通过构造方法传入过滤器处理路径
         * @param handlePath 过滤器处理路径
         */
        MyFileFilter(HandlePath handlePath) {
            this.handlePath = handlePath;
        }

        /**
         * 过滤器处理的实现方法
         * @param file 要处理的文件
         * @return 是否已经处理
         */
        @Override
        public boolean accept(File file) {
            Set<String> excludePath = handlePath.getExcludePath();
            for(String exclude:excludePath)
                if (file.getAbsolutePath().startsWith(exclude)) return false;
            return true;
        }
    }
}