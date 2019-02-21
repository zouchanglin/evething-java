package com.xpu.everything.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;


@Getter
@ToString
public final class EverythingConfig {
    private static volatile EverythingConfig config;

    /**
     * 索引的路径
     */
    private Set<String> includePath = PropertiesParse.getIncludePath();

    /**
     * 排除索引的路径
     */
    private Set<String> excludePath = PropertiesParse.getExcludePath();

    /**
     * H2数据库文件路径
     */
    private String h2IndexPath = PropertiesParse.getDbPath();

    /**
     * 检索返回最大的结果数量
     */
    @Setter
    private Integer maxReturn = PropertiesParse.getMaxReturn();

    /**
     * 优先排出深度比较浅的,从浅到深返回结果
     */
    @Setter
    private Boolean deptOrderAsc = PropertiesParse.getDeptOrderAsc();

    private EverythingConfig(){
    }

    /**
     * 初始化默认配置
     */
    private static void initDefaultPathsConfig(){
        //遍历的目录
    }

    public static EverythingConfig getInstance(){
        if(config == null){
            synchronized (EverythingConfig.class){
                if(config == null){
                    config = new EverythingConfig();
                    initDefaultPathsConfig();
                }
            }
        }
        return config;
    }
}
