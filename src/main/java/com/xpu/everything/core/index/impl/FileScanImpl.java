package com.xpu.everything.core.index.impl;

import com.xpu.everything.config.EverythingConfig;
import com.xpu.everything.core.index.FileScan;
import com.xpu.everything.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;

/**
 * 文件扫描接口FileScan的实现类
 */
public class FileScanImpl implements FileScan {
    /**
     * 配饰管理器实例对象
     */
    private EverythingConfig config = EverythingConfig.getInstance();

    /**
     * 文件拦截器集合
     */
    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();

    /**
     * 带拦截器的索引，把在排除目录的文件排除
     * @param path 需要过滤的的文件或者文件夹
     */
    @Override
    public void index(String path) {
        File file = new File(path);
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getParent())){
                return;
            }
        }else{
            if(config.getExcludePath().contains(path)){
                return;
            }else{
                File[] files = file.listFiles();
                if(files != null){
                    for(File f:files){
                        index(f.getAbsolutePath());
                    }
                }
            }
        }

        //遍历拦截下来的文件进入apply
        for(FileInterceptor interceptor: this.interceptors){
            interceptor.apply(file);
        }
    }

    /**
     * 将文件拦截器加入到文件拦截器集合
     * @param interceptor 需要传入的拦截器
     */
    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}