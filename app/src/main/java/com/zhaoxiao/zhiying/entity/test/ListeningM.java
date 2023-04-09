package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class ListeningM extends QuestionM {
    private String audio;
    private int type;
    private List<ListeningQuestion> listeningQuestionList;

    public ListeningM() {
    }

    public ListeningM(int id, String info, String audio, int type, int subQuestionNum, List<ListeningQuestion> listeningQuestionList) {
        super(id, info, subQuestionNum);
        this.audio = audio;
        this.type = type;
        this.listeningQuestionList = listeningQuestionList;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ListeningQuestion> getListeningQuestionList() {
        return listeningQuestionList;
    }

    public void setListeningQuestionList(List<ListeningQuestion> listeningQuestionList) {
        this.listeningQuestionList = listeningQuestionList;
    }
}
