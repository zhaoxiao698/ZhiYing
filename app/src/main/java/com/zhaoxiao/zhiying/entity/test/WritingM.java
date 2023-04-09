package com.zhaoxiao.zhiying.entity.test;

public class WritingM extends QuestionM {
    private String img;
    private int type;
    private String answer;
    private String userAnswer;

    public WritingM() {
    }

    public WritingM(int id, String info, String img, int type, String answer) {
        super(id, info, 0);
        this.img = img;
        this.type = type;
        this.answer = answer;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
