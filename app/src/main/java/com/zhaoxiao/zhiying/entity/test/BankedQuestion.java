package com.zhaoxiao.zhiying.entity.test;

public class BankedQuestion extends SubQuestion {
    private int bankedId;

    public BankedQuestion() {
    }

    public BankedQuestion(int id, int bankedId, String stem, String answer) {
        super(id, stem, answer);
        this.bankedId = bankedId;
    }

    public int getBankedId() {
        return bankedId;
    }

    public void setBankedId(int bankedId) {
        this.bankedId = bankedId;
    }
}
