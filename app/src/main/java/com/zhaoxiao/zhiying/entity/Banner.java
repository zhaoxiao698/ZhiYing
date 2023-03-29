package com.zhaoxiao.zhiying.entity;

import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("id")
    private int id;
    @SerializedName("contentId")
    private int contentId;
    @SerializedName("img")
    private String img;
    @SerializedName("type")
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", img='" + img + '\'' +
                ", type=" + type +
                '}';
    }
}
