package com.xpu.everything.config;


import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 配置解析器，此类主要是解析配置文件
 */
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

    /**
     * 获取显示的最大返回值
     * @return 显示的最大返回值
     */
    public static int getMaxReturn(){
        //获取key对应的value值
        String exclude = properties.getProperty("maxReturn");
        return Integer.parseInt(exclude);
    }

    /**
     * 获取排除的路径
     * @return 排除的路径,Set<String>类型
     */
    static Set<String> getExcludePath(){
        String exclude = properties.getProperty("exclude");
        String[] split = exclude.split(",");
        return new HashSet<>(Arrays.asList(split));
    }
    /**
     * 获取包含的路径
     * @return 包含的路径,Set<String>类型
     */
    static Set<String> getIncludePath(){
        String exclude = properties.getProperty("include");
        String[] split = exclude.split(",");
        return new HashSet<>(Arrays.asList(split));
    }

    /**
     * 文件深度排序
     * @return 排升/降序
     */
    static Boolean getDeptOrderAsc(){
        return Boolean.parseBoolean(properties.getProperty("deptOrderAsc"));
    }

    /**
     * 获取文件监视系统的扫描时间间隔
     * @return 文件监视系统的扫描时间间隔（返回值为毫秒值）
     */
    public static int getIntervalTime(){
        try{
            return Integer.parseInt(properties.getProperty("intervalTime"));
        }catch (NumberFormatException e){
            System.out.println("配置文件格式有误");
            return 1000;
        }
    }

    /**
     * 获取数据库路径
     * @return 数据库路径
     */
    static String getDbPath(){
        return properties.getProperty("dbPath");
    }
}
