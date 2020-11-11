package com.demo.appmonitor.model;

import android.graphics.Bitmap;

public class ProfileActivityItem {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private int imageId;
    private String text;

    public ProfileActivityItem(String name, int imageId, String text) {
        this.name = name;
        this.imageId = imageId;
        this.text = text;
    }

}
