package com.zhaoxiao.zhiying.entity.community;

import com.google.gson.annotations.SerializedName;
import com.zhaoxiao.zhiying.entity.mine.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Trend implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userAvatar")
    private String userAvatar;
    @SerializedName("title")
    private String title;
    @SerializedName("info")
    private String info;
    @SerializedName("topicList")
    private List<Topic> topicList;
    @SerializedName("userList")
    private List<User> userList;
    @SerializedName("imgList")
    private List<ImageViewInfo> imgList;
    @SerializedName("addTime")
    private Date addTime;
    @SerializedName("like")
    private int like;
    @SerializedName("collection")
    private int collection;
    @SerializedName("comment")
    private int comment;
    @SerializedName("share")
    private int share;
    @SerializedName("hotComment")
    private Comment hotComment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<ImageViewInfo> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImageViewInfo> imgList) {
        this.imgList = imgList;
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

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public Comment getHotComment() {
        return hotComment;
    }

    public void setHotComment(Comment hotComment) {
        this.hotComment = hotComment;
    }

    @Override
    public String toString() {
        return "Trend{" +
                "id=" + id +
                ", userAccount='" + userAccount + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", topicList=" + topicList +
                ", userList=" + userList +
                ", imgList=" + imgList +
                ", addTime=" + addTime +
                ", like=" + like +
                ", collection=" + collection +
                ", comment=" + comment +
                ", share=" + share +
                ", hotComment=" + hotComment +
                '}';
    }
}
