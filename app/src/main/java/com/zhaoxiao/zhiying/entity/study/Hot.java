package com.zhaoxiao.zhiying.entity.study;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Hot {
    @SerializedName("id")
    private int id;
    @SerializedName("ftypeName")
    private String ftypeName;
    @SerializedName("stypeName")
    private String stypeName;
    @SerializedName("name")
    private String name;
    @SerializedName("img")
    private String img;
    @SerializedName("num")
    private int num;
    @SerializedName("collection")
    private int collection;
    @SerializedName("lastTime")
    private Date lastTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFtypeName() {
        return ftypeName;
    }

    public void setFtypeName(String ftypeName) {
        this.ftypeName = ftypeName;
    }

    public String getStypeName() {
        return stypeName;
    }

    public void setStypeName(String stypeName) {
        this.stypeName = stypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "Hot{" +
                "id=" + id +
                ", ftypeName='" + ftypeName + '\'' +
                ", stypeName='" + stypeName + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", num=" + num +
                ", collection=" + collection +
                ", lastTime=" + lastTime +
                '}';
    }
}
