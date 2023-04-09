package com.zhaoxiao.zhiying.entity.test;

import java.io.Serializable;

public class SubQuestion implements Serializable {
    private int id;
    private String stem;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
    private int order;
    private String userAnswer;
    public SubQuestion() {
    }

    public SubQuestion(int id, String stem, String answer) {
        this.id = id;
        this.stem = stem;
        this.answer = answer;
    }

    public SubQuestion(int id, String stem, String a, String b, String c, String d, String answer) {
        this.id = id;
        this.stem = stem;
        A = a;
        B = b;
        C = c;
        D = d;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
