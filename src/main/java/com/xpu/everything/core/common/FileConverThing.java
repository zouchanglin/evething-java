package com.xpu.everything.core.common;

import com.xpu.everything.core.model.FileType;
import com.xpu.everything.core.model.Thing;

import java.io.File;

/**
 * 辅助工具类：将file对象转换成Thing对象
 */
public final class FileConverThing {
    private FileConverThing() {}

    /**
     * File对象转换成Thing对象
     * @param file File对象
     * @return 转换后的Thing
     */
    public static Thing convert(File file){
        Thing thing = new Thing();
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());
        thing.setDepth(calcFileDepth(file));
        thing.setFileType(calcFileType(file));
        return thing;
    }


    /**
     * 计算文件深度
     * @param file 要计算的文件
     * @return 文件深度
     */
    private static int calcFileDepth(File file){
        String[] split = file.getAbsolutePath().split("\\\\");
        return split.length;
    }

    /**
     * 获取文件类型
     * @param file 需要判断的文件
     * @return 该文件的类型
     */
    private static FileType calcFileType(File file){
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        String filename = file.getName();
        int index = filename.lastIndexOf(".");
        //假设文件名是这样的： abc.
        if(index != -1 && index < filename.length() - 1){
            String extend = filename.substring(index + 1);
            return FileType.lookup(extend);
        }else{
            return FileType.OTHER;
        }

    }
}
