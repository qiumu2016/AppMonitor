package com.demo.appmonitor.model;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppItem {
    private String appName; // 应用名
    private String packageName; // 包名
    private String image; // App的Icon
    private String type;      // 用来判断是否是系统应用

    public AppItem(String appName, String packageName, String image, String type) {
        this.appName = appName;
        this.packageName = packageName;
        this.image = image;
        this.type = type;
    }

    public AppItem() { }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /* 静态方法：判断应用类型 */
    public static String getAppTypeCN(int flags){
        if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            return "系统应用";
        }
        // 非系统应用
        if ((flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
            return "第三方应用";
        }
        // 系统应用，但更新后变成不是系统应用了
        if ((flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return "第三方应用";
        }

        if (flags == ApplicationInfo.FLAG_SYSTEM) {
            return "SD卡应用";
        }
        return "未知应用";
    }
}
