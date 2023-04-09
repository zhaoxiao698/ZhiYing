package com.zhaoxiao.zhiying.entity.test;

public class NewQuestion extends SubQuestion {
    private int newId;

    public NewQuestion() {
    }

    public NewQuestion(int id, int newId, String stem, String answer) {
        super(id, stem, answer);
        this.newId = newId;
    }

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }
}
