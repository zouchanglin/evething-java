package com.xpu.everything.cmd;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 此类用于记录用户的输入命令，存储到文件中
 */
class History {
    /**
     * 记录用户的输入命令
     * @param cmd 用户的输入命令
     */
    static void pushHistory(String cmd){
        try {
            FileUtils.write(new File("history.txt"),cmd+"\r\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
