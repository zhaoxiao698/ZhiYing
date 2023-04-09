package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class ClozeM extends QuestionM {
    private List<ClozeQuestion> clozeQuestionList;

    public ClozeM() {
    }

    public ClozeM(int id, String info, int subQuestionNum, List<ClozeQuestion> clozeQuestionList) {
        super(id, info, subQuestionNum);
        this.clozeQuestionList = clozeQuestionList;
    }

    public List<ClozeQuestion> getClozeQuestionList() {
        return clozeQuestionList;
    }

    public void setClozeQuestionList(List<ClozeQuestion> clozeQuestionList) {
        this.clozeQuestionList = clozeQuestionList;
    }
}
