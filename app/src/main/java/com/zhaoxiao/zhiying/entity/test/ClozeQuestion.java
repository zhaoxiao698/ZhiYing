package com.zhaoxiao.zhiying.entity.test;

public class ClozeQuestion extends SubQuestion {
    private int clozeId;

    public ClozeQuestion() {
    }

    public ClozeQuestion(int id, int clozeId, String stem, String a, String b, String c, String d, String answer) {
        super(id, stem, a, b, c, d, answer);
        this.clozeId = clozeId;
    }

    public int getClozeId() {
        return clozeId;
    }

    public void setClozeId(int clozeId) {
        this.clozeId = clozeId;
    }
}
