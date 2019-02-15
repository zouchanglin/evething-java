package com.xpu.everything.core.interceptor;

import java.io.File;

/**
 * 函数接口
 */
@FunctionalInterface
public interface FileInterceptor {
    void apply(File file);
}
