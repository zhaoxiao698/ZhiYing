package com.zhaoxiao.zhiying.entity.study;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ArticleNote {
    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("articleId")
    private int articleId;
    @SerializedName("info")
    private String info;
    @SerializedName("addTime")
    private Date addTime;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
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
}
