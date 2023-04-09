package com.zhaoxiao.zhiying.entity.test;

public class ListeningQuestion extends SubQuestion {
    private int listeningId;

    public ListeningQuestion() {
    }

    public ListeningQuestion(int id, int listeningId, String stem, String a, String b, String c, String d, String answer) {
        super(id, stem, a, b, c, d, answer);
        this.listeningId = listeningId;
    }

    public int getListeningId() {
        return listeningId;
    }

    public void setListeningId(int listeningId) {
        this.listeningId = listeningId;
    }
}
