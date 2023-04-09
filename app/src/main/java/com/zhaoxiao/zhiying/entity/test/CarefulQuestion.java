package com.zhaoxiao.zhiying.entity.test;

public class CarefulQuestion extends SubQuestion {
    private int carefulId;

    public CarefulQuestion() {
    }

    public CarefulQuestion(int id, int carefulId, String stem, String a, String b, String c, String d, String answer) {
        super(id, stem, a, b, c, d, answer);
        this.carefulId = carefulId;
    }

    public int getCarefulId() {
        return carefulId;
    }

    public void setCarefulId(int carefulId) {
        this.carefulId = carefulId;
    }
}
