package com.zhaoxiao.zhiying.entity.test;

public class MatchQuestion extends SubQuestion {
    private int matchId;

    public MatchQuestion() {
    }

    public MatchQuestion(int id, int matchId, String stem, String answer) {
        super(id, stem, answer);
        this.matchId = matchId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
}
