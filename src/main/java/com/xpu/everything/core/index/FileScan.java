package com.xpu.everything.core.index;

import com.xpu.everything.core.dao.DataSourceFactory;
import com.xpu.everything.core.dao.impl.FileIndexImpl;
import com.xpu.everything.core.index.impl.FileScanImpl;
import com.xpu.everything.core.interceptor.FileIndexInterceptor;
import com.xpu.everything.core.interceptor.FileInterceptor;
import com.xpu.everything.core.interceptor.FilePrintInterceptor;

public interface FileScan {
    /**
     * 建立索引,遍历path
     * @param需要遍历的路径
     */
    void index(String path);

    /**
     * 遍历的拦截器
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);


    public static void main(String[] args) {
        //DataSourceFactory.initDatabase();
        FileScanImpl sc = new FileScanImpl();
        FileInterceptor filePrintInterceptor = new FilePrintInterceptor();
        FileInterceptor fileIndexInterceptor = new FileIndexInterceptor(
                new FileIndexImpl(DataSourceFactory.getDataSource()));

        sc.interceptor(fileIndexInterceptor);
        sc.interceptor(filePrintInterceptor);

        sc.index("D:\\JavaCode");
    }
}
