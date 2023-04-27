package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class QuestionAnswer {
    private int questionId;
    private String answer;
    private int table;
    private String account;
    private boolean right;
    private List<SubQuestionAnswer> subQuestionAnswerList;

    public QuestionAnswer() {
    }

    public QuestionAnswer(int questionId, String answer, int table, String account, boolean right, List<SubQuestionAnswer> subQuestionAnswerList) {
        this.questionId = questionId;
        this.answer = answer;
        this.table = table;
        this.account = account;
        this.right = right;
        this.subQuestionAnswerList = subQuestionAnswerList;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public List<SubQuestionAnswer> getSubQuestionAnswerList() {
        return subQuestionAnswerList;
    }

    public void setSubQuestionAnswerList(List<SubQuestionAnswer> subQuestionAnswerList) {
        this.subQuestionAnswerList = subQuestionAnswerList;
    }
}
