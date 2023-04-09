package com.zhaoxiao.zhiying.entity.study;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Recent {
    @SerializedName("id")
    private int id;
    @SerializedName("channelName")
    private String channelName;
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private String duration;
    @SerializedName("img")
    private String img;
    @SerializedName("addTime")
    private Date addTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Recent{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", img='" + img + '\'' +
                ", addTime='" + addTime + '\'' +
                '}';
    }
}
