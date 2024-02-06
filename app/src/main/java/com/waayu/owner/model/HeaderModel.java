package com.waayu.owner.model;

public class HeaderModel {
    String getTitle,getSize;

    public String getGetTitle() {
        return getTitle;
    }

    public String getGetSize() {
        return getSize;
    }

    public void setGetTitle(String getTitle) {
        this.getTitle = getTitle;
    }

    public HeaderModel(String getTitle, String getSize) {
        this.getTitle = getTitle;
        this.getSize = getSize;
    }

    public void setGetSize(String getSize) {
        this.getSize = getSize;
    }
}
