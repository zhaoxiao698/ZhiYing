package com.zhaoxiao.zhiying.entity.test;

import com.google.gson.annotations.SerializedName;

public class TruePaper {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("questionBankId")
    private int questionBankId;
    @SerializedName("questionBankName")
    private String questionBankName;

    public TruePaper() {
    }

    public TruePaper(int id, String name, int questionBankId) {
        this.id = id;
        this.name = name;
        this.questionBankId = questionBankId;
    }

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

    public int getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(int questionBankId) {
        this.questionBankId = questionBankId;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName;
    }
}
