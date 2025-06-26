package com.xiaomi.types.model;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * ClassName: VidGenerator
 * Package: com.xiaomi.types.model
 */
public class VidGenerator {
    private static final String VIN_PREFIX = "VIN";

    public static String generate() {
        // 生成13位随机字符（3位前缀 + 13位随机 = 16位）
        return VIN_PREFIX + RandomStringUtils.randomAlphanumeric(13).toUpperCase();
    }
}