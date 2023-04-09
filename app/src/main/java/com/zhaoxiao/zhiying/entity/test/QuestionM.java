package com.zhaoxiao.zhiying.entity.test;

import java.io.Serializable;
import java.util.List;

public class QuestionM implements Serializable {
    private int id;
    private String info;
    private int subQuestionNum;
    private int order;

    public QuestionM() {
    }

    public QuestionM(int id, String info, int subQuestionNum) {
        this.id = id;
        this.info = info;
        this.subQuestionNum = subQuestionNum;
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

    public int getSubQuestionNum() {
        return subQuestionNum;
    }

    public void setSubQuestionNum(int subQuestionNum) {
        this.subQuestionNum = subQuestionNum;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
