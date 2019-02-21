package com.xpu.everything.config;


import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropertiesParse {
    private static Properties properties;

    static{
        properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesParse.class.getClassLoader()
                .getResourceAsStream("setting.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static int getMaxReturn(){
        //获取key对应的value值
        String exclude = properties.getProperty("maxReturn");
        return Integer.parseInt(exclude);
    }


    static Set<String> getExcludePath(){
        String exclude = properties.getProperty("exclude");
        String[] split = exclude.split(",");
        return new HashSet<>(Arrays.asList(split));
    }

    static Set<String> getIncludePath(){
        String exclude = properties.getProperty("include");
        String[] split = exclude.split(",");
        return new HashSet<>(Arrays.asList(split));
    }

    static Boolean getDeptOrderAsc(){
        return Boolean.parseBoolean(properties.getProperty("deptOrderAsc"));
    }

    public static int getIntervalTime(){
        try{
            return Integer.parseInt(properties.getProperty("intervalTime"));
        }catch (NumberFormatException e){
            System.out.println("配置文件格式有误");
            return 1000;
        }
    }

    static String getDbPath(){
        return properties.getProperty("dbPath");
    }
}
