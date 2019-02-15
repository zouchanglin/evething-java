package com.xpu.everything.core.index.impl;

import com.xpu.everything.config.EverythingConfig;
import com.xpu.everything.core.index.FileScan;
import com.xpu.everything.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;

public class FileScanImpl implements FileScan {
    private EverythingConfig config = EverythingConfig.getInstance();

    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();


    @Override
    public void index(String path) {
        File file = new File(path);
        //List<File> filelist = new ArrayList<>();

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

        //File Dircetory
        for(FileInterceptor interceptor: this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}