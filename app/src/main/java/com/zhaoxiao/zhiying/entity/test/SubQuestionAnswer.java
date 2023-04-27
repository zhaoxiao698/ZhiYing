package com.zhaoxiao.zhiying.entity.test;

public class SubQuestionAnswer {
    private int subQuestionId;
    private String answer;
    private boolean right;

    public SubQuestionAnswer() {
    }

    public SubQuestionAnswer(int subQuestionId, String answer, boolean right) {
        this.subQuestionId = subQuestionId;
        this.answer = answer;
        this.right = right;
    }

    public int getSubQuestionId() {
        return subQuestionId;
    }

    public void setSubQuestionId(int subQuestionId) {
        this.subQuestionId = subQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
