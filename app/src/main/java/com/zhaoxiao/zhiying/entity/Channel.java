package com.zhaoxiao.zhiying.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Channel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("info")
    private String info;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", img='" + img + '\'' +
                ", num=" + num +
                ", collection=" + collection +
                ", lastTime='" + lastTime + '\'' +
                '}';
    }
}
