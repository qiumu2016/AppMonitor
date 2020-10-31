package com.demo.appmonitor.domain;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 * App 类，用来封装app的信息
 */
public class AppInfo {

    private String appName; // 应用名
    private String packageName; // 包名
    private Drawable appIcon; // App的Icon
    private int flags;      // 用来判断是否是系统应用

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    /* 判断应用类型 */
    public String getAppTypeCN(){
        if ((this.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            return "系统应用";
        }
        // 非系统应用
        if ((this.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
            return "第三方应用";
        }
        // 系统应用，但更新后变成不是系统应用了
        if ((this.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return "第三方应用";
        }

        if (this.flags == ApplicationInfo.FLAG_SYSTEM) {
            return "SD卡应用";
        }
        return "未知应用";
    }
}
