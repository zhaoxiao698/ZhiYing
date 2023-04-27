package com.zhaoxiao.zhiying.entity.mine;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Plan implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("plan")
    private long plan;
    @SerializedName("planDo")
    private long planDo;
    @SerializedName("addTime")
    private Date addTime;

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

    public long getPlan() {
        return plan;
    }

    public void setPlan(long plan) {
        this.plan = plan;
    }

    public long getPlanDo() {
        return planDo;
    }

    public void setPlanDo(long planDo) {
        this.planDo = planDo;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
