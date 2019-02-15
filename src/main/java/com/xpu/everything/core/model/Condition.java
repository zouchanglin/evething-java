package com.xpu.everything.core.model;

import lombok.Data;

/**
 * 检索的参数
 */
@Data
public class Condition {
    /**
     * 检索的文件名
     */
    private String name;

    /**
     * 检索的文件类型
     */
    private String filetype;

    /**
     * 查询的深度
     */
    private Integer limit;


    /**
     * 检索结果的文件信息depth排序规则
     * 默认是true--》asc
     * false--》dsc
     */
    private Boolean orderByAsc;
}
