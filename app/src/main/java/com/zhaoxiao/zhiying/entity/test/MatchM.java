package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class MatchM extends QuestionM {
    private int num;
    private List<MatchQuestion> matchQuestionList;

    public MatchM() {
    }

    public MatchM(int id, String info, int num, int subQuestionNum, List<MatchQuestion> matchQuestionList) {
        super(id, info, subQuestionNum);
        this.num = num;
        this.matchQuestionList = matchQuestionList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<MatchQuestion> getMatchQuestionList() {
        return matchQuestionList;
    }

    public void setMatchQuestionList(List<MatchQuestion> matchQuestionList) {
        this.matchQuestionList = matchQuestionList;
    }
}
