package com.zhaoxiao.zhiying.entity.mine;

import com.google.gson.annotations.SerializedName;

public class CodeResponse {
    @SerializedName("result")
    private int result;
    @SerializedName("desc")
    private String desc;
    @SerializedName("msgid")
    private long msgid;
    @SerializedName("custid")
    private String custid;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getMsgid() {
        return msgid;
    }

    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    @Override
    public String toString() {
        return "CodeResponse{" +
                "result=" + result +
                ", desc='" + desc + '\'' +
                ", msgid=" + msgid +
                ", custid='" + custid + '\'' +
                '}';
    }
}
