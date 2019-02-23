package com.xpu.everything.core.interceptor;

import java.io.File;

/**
 * 文件打印过滤器，实现FileInterceptor，主要功能是打印文件的路径
 * 注意：这里打印的是已经过滤后的文件
 */
public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println("FilePath-->"+file.getAbsolutePath());
    }
}
