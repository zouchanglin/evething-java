package com.xpu.everything.core.monitor;

import com.xpu.everything.core.common.HandlePath;
import org.apache.commons.io.monitor.FileAlterationMonitor;

public interface FileWatch {
    /**
     * 监听的目录
     */
    void monitor(HandlePath handlePath);


    FileAlterationMonitor getFileAlterationMonitor();
}
