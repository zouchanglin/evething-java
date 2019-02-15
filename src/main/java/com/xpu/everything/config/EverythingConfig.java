package com.xpu.everything.config;

import lombok.Getter;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Getter
public final class EverythingConfig {
    private static volatile EverythingConfig config;

    /**
     * 索引的路径
     */
    private Set<String> includePath = new HashSet<>();

    /**
     * 排除索引的路径
     */
    private Set<String> excludePath = new HashSet<>();


    private EverythingConfig(){}

    public static EverythingConfig getInstance(){
        if(config == null){
            synchronized (EverythingConfig.class){
                if(config == null){
                    config = new EverythingConfig();
                    //遍历的目录
                    FileSystem fileSystem = FileSystems.getDefault();
                    Iterable<Path> directories = fileSystem.getRootDirectories();
                    directories.forEach(path -> config.getIncludePath().add(path.toString()));

                    //排除的目录
                    //windows C:\Windows C:\Program Files C:\Program Files (x86) C:\ProgramData
                    //linux /tmp /etc/
                    String os_name = System.getProperty("os.name");
                    if(os_name.startsWith("Windows")){
                        config.getExcludePath().add("C:\\Windows");
                        config.getExcludePath().add("C:\\Program Files");
                        config.getExcludePath().add("C:\\Program Files (x86)");
                        config.getExcludePath().add("C:\\ProgramData");

                    }else if(os_name.startsWith("Linux")){
                        config.getExcludePath().add("/tmp");
                        config.getExcludePath().add("/etc");
                        config.getExcludePath().add("/root");

                    }else{
                        config.getExcludePath().add("/tmp");
                        config.getExcludePath().add("/etc");
                    }

                }
            }
        }
        return config;
    }

    public static void main(String[] args) {
        EverythingConfig everythingConfig = EverythingConfig.getInstance();
        Set<String> includePath = everythingConfig.getIncludePath();

        Set<String> excludePath = everythingConfig.getExcludePath();

        System.out.println(includePath);
        System.out.println(excludePath);
    }
}
