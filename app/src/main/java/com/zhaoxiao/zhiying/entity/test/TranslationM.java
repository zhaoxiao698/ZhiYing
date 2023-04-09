package com.zhaoxiao.zhiying.entity.test;

public class TranslationM extends QuestionM{
    private String answer;
    private String userAnswer;

    public TranslationM() {
    }

    public TranslationM(int id, String info, String answer) {
        super(id, info, 0);
        this.answer = answer;
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
