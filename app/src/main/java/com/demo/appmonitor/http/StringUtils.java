package com.demo.appmonitor.http;

import androidx.annotation.Nullable;

import java.util.List;

public class StringUtils {
    public static boolean isBlank(@Nullable String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlankList(@Nullable List list) {
        return list == null || list.size() == 0;
    }
}
