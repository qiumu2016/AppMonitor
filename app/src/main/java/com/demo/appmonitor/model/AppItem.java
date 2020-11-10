package com.demo.appmonitor.model;

import android.graphics.drawable.Drawable;

public class AppItem {
    private String appName; // 应用名
    private String packageName; // 包名
    private Drawable appIcon; // App的Icon
    private String type;      // 用来判断是否是系统应用

    public AppItem(String appName, String packageName, Drawable appIcon, String type) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.type = type;
    }

    public AppItem() {
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
