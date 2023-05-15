package com.zhaoxiao.zhiying.entity.mine;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    @SerializedName("account")
    private String account;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("age")
    private int age;
    @SerializedName("sex")
    private String sex;
    @SerializedName("permissions")
    private int permissions;
    @SerializedName("addTime")
    private Date addTime;
    @SerializedName("status")
    private int status;
    @SerializedName("sign")
    private String sign;
    @SerializedName("mail")
    private String mail;
    @SerializedName("trendNum")
    private int trendNum;
    @SerializedName("attentionNum")
    private int attentionNum;
    @SerializedName("fanNum")
    private int fanNum;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTrendNum() {
        return trendNum;
    }

    public void setTrendNum(int trendNum) {
        this.trendNum = trendNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public int getFanNum() {
        return fanNum;
    }

    public void setFanNum(int fanNum) {
        this.fanNum = fanNum;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", permissions=" + permissions +
                ", addTime=" + addTime +
                ", status=" + status +
                ", sign='" + sign + '\'' +
                ", mail='" + mail + '\'' +
                ", trendNum=" + trendNum +
                ", attentionNum=" + attentionNum +
                ", fanNum=" + fanNum +
                '}';
    }
}
