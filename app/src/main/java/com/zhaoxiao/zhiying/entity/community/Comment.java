package com.zhaoxiao.zhiying.entity.community;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("trendId")
    private int trendId;
    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userAvatar")
    private String userAvatar;
    @SerializedName("info")
    private String info;
    @SerializedName("addTime")
    private Date addTime;
    @SerializedName("like")
    private int like;
    @SerializedName("likeStatus")
    private boolean likeStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrendId() {
        return trendId;
    }

    public void setTrendId(int trendId) {
        this.trendId = trendId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public boolean getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", trendId=" + trendId +
                ", userAccount='" + userAccount + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", info='" + info + '\'' +
                ", addTime=" + addTime +
                ", like=" + like +
                ", likeStatus=" + likeStatus +
                '}';
    }
}
