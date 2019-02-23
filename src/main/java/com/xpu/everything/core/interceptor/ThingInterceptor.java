package com.xpu.everything.core.interceptor;

import com.xpu.everything.core.model.Thing;

/**
 * 执行不存在文件在数据库中的清理工作
 */
@FunctionalInterface
public interface ThingInterceptor {
    void apply(Thing thing);
}
