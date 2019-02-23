package com.xpu.everything.core.interceptor;

import java.io.File;

/**
 * 文件过滤器函数接口
 */
@FunctionalInterface
public interface FileInterceptor {
    void apply(File file);
}
