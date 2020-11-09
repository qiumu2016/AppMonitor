package com.demo.appmonitor.model;

public class ResearchItem {
    private String imageId;
    private String appName;
    private String packageName;
    private String lastTime;

    public ResearchItem(){};

    public ResearchItem(String imageId, String appName, String packageName, String lastTime) {
        this.imageId = imageId;
        this.appName = appName;
        this.packageName = packageName;
        this.lastTime = lastTime;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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
