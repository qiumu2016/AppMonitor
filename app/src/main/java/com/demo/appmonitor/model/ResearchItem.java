package com.demo.appmonitor.model;

import android.graphics.drawable.Drawable;

public class ResearchItem {
    private Drawable image;
    private String appName;
    private String packageName;
    private String lastTime;

    public ResearchItem(){};

    public ResearchItem(Drawable image, String appName, String packageName, String lastTime) {
        this.image = image;
        this.appName = appName;
        this.packageName = packageName;
        this.lastTime = lastTime;
    }

    public Drawable getImageId() {
        return image;
    }

    public void setImageId(Drawable imageId) {
        this.image = imageId;
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

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
