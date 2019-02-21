package com.xpu.everything.core.interceptor;

import com.xpu.everything.core.model.Thing;

@FunctionalInterface
public interface ThingInterceptor {
    void apply(Thing thing);
}
