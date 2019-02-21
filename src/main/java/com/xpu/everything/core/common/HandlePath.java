package com.xpu.everything.core.common;

import lombok.Data;

import java.util.Set;

/**
 * 包含的目录和不包含的目录
 */
@Data
public class HandlePath {
    private Set<String> includePath;
    private Set<String> excludePath;
}
