package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class CarefulM extends QuestionM {
    private List<CarefulQuestion> carefulQuestionList;

    public CarefulM() {
    }

    public CarefulM(int id, String info, int subQuestionNum, List<CarefulQuestion> carefulQuestionList) {
        super(id, info, subQuestionNum);
        this.carefulQuestionList = carefulQuestionList;
    }

    public List<CarefulQuestion> getCarefulQuestionList() {
        return carefulQuestionList;
    }

    public void setCarefulQuestionList(List<CarefulQuestion> carefulQuestionList) {
        this.carefulQuestionList = carefulQuestionList;
    }
}
