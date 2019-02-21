package com.xpu.everything.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件类型
 */
public enum FileType {
    /**
     * 这些是文件的类型
     */
    IMG("png", "jpeg", "jpg", "gif", "bmp"),
    /**
     * 代码文件
     */
    SRC("java", "c", "cpp", "h", "hpp", "cc", "py"),
    /**
     * 文档
     */
    DOC("ppt", "pptx", "doc","docx","pdf", "xls", "xlsx", "pdf", "txt", "md"),

    /**
     * 二进制可执行
     */
    BIN("exe", "sh", "jar", "msi"),
    /**
     * 归档
     */
    ARCHIVE("rar", "zip", "7z"),

    /**
     * 其他类型
     */
    OTHER("*");


    /**s
     * 对应的文件类型的扩展名集合
     */
    private Set<String> extend = new HashSet<>();

    FileType(String... extend){
        this.extend.addAll(Arrays.asList(extend));
    }

    /**
     * 根据文件扩展名获取文件类型
     * @param extend 文件扩展名
     * @return 文件的类型（如下）：
     * IMG("png", "jpeg", "jpg", "gif", "bmp"),
     * SRC("java", "c", "cpp", "h", "hpp", "cc", "py"),
     * DOC("ppt", "pptx", "doc","docx","pdf", "xls", "xlsx", "pdf", "txt", "md"),
     * BIN("exe", "sh", "jar", "msi"),
     * ARCHIVE("rar", "zip", "7z"),
     * OTHER("*");
     * 不存在上面的类型则返回OTHER
     */
    public static FileType lookup(String extend){
        for (FileType filetype: FileType.values()) {
            if(filetype.extend.contains(extend)){
                return filetype;
            }
        }
        return FileType.OTHER;
    }

    /**
     * 根据文件类型名（String）获取文件类型对象
     * @param name 要查找的文件名
     * @return 返回文件类型
     */
    public static FileType lookupByName(String name){
        for (FileType filetype: FileType.values()) {
            if(filetype.name().equals(name)){
                return filetype;
            }
        }
        return FileType.OTHER;
    }
}
