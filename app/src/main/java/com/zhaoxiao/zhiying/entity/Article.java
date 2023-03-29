package com.zhaoxiao.zhiying.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Article {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private String duration;
    @SerializedName("img")
    private String img;
    @SerializedName("count")
    private int count;
    @SerializedName("collection")
    private int collection;
    @SerializedName("addTime")
    private Date addTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", img='" + img + '\'' +
                ", count=" + count +
                ", collection=" + collection +
                ", addTime='" + addTime + '\'' +
                '}';
    }
}
