package com.xpu.everything.core.interceptor;

import java.io.File;

public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println("FilePath-->"+file.getAbsolutePath());
    }
}
