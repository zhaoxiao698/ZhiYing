package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class NewM extends QuestionM {
    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private int type;
    private List<NewQuestion> newQuestionList;

    public NewM() {
    }

    public NewM(int id, String info, String a, String b, String c, String d, String e, String f, String g, int type, int subQuestionNum, List<NewQuestion> newQuestionList) {
        super(id, info, subQuestionNum);
        A = a;
        B = b;
        C = c;
        D = d;
        E = e;
        F = f;
        G = g;
        this.type = type;
        this.newQuestionList = newQuestionList;
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

    public String getE() {
        return E;
    }

    public void setE(String e) {
        E = e;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }

    public String getG() {
        return G;
    }

    public void setG(String g) {
        G = g;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<NewQuestion> getNewQuestionList() {
        return newQuestionList;
    }

    public void setNewQuestionList(List<NewQuestion> newQuestionList) {
        this.newQuestionList = newQuestionList;
    }
}
