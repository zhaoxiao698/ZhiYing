package com.zhaoxiao.zhiying.entity.test;

import java.util.List;

public class BankedM extends QuestionM {
    private String word;
    private String[] wordList;
    private List<BankedQuestion> bankedQuestionList;

    public BankedM() {
    }

    public BankedM(int id, String info, String word, String[] wordList, int subQuestionNum, List<BankedQuestion> bankedQuestionList) {
        super(id, info, subQuestionNum);
        this.word = word;
        this.wordList = wordList;
        this.bankedQuestionList = bankedQuestionList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getWordList() {
        return wordList;
    }

    public void setWordList(String[] wordList) {
        this.wordList = wordList;
    }

    public List<BankedQuestion> getBankedQuestionList() {
        return bankedQuestionList;
    }

    public void setBankedQuestionList(List<BankedQuestion> bankedQuestionList) {
        this.bankedQuestionList = bankedQuestionList;
    }
}
