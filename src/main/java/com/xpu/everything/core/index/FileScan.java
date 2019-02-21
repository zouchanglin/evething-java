package com.xpu.everything.core.index;

import com.xpu.everything.core.interceptor.FileInterceptor;

public interface FileScan {
    /**
     * 建立索引,遍历path
     * @param需要遍历的路径
     */
    void index(String path);

    /**
     * 遍历的拦截器
     * @param interceptor 需要传入的拦截器
     */
    void interceptor(FileInterceptor interceptor);
}
