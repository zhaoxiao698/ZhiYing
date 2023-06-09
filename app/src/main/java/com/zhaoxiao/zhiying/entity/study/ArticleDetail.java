package com.zhaoxiao.zhiying.entity.study;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ArticleDetail {
    @SerializedName("id")
    private int id;
    @SerializedName("channelId")
    private Integer channelId;
    @SerializedName("channelName")
    private String channelName;
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private String duration;
    @SerializedName("img")
    private String img;
    @SerializedName("audio")
    private String audio;
    @SerializedName("video")
    private String video;
    @SerializedName("count")
    private int count;
    @SerializedName("collection")
    private int collection;
    @SerializedName("addTime")
    private Date addTime;
    @SerializedName("sentenceList")
    private List<Sentence> sentenceList;
    @SerializedName("collect")
    private boolean collect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    public void setSentenceList(List<Sentence> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public boolean getCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }
}
