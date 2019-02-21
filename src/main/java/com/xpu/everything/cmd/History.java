package com.xpu.everything.cmd;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

class History {
    static void pushHistory(String cmd){
        try {
            FileUtils.write(new File("history.txt"),cmd+"\r\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
