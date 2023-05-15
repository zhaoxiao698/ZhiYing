package com.zhaoxiao.zhiying.entity.mine;

import java.util.Date;

public class Message {
    private int id;
    private String info;
    private String sendAccount;
    private String receiveAccount;
    private Date addTime;

    public Message() {
    }

    public Message(int id, String info, String sendAccount, String receiveAccount, Date addTime) {
        this.id = id;
        this.info = info;
        this.sendAccount = sendAccount;
        this.receiveAccount = receiveAccount;
        this.addTime = addTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
