package com.zhaoxiao.zhiying.entity.word;

import com.google.gson.annotations.SerializedName;

public class BookMore {
    @SerializedName("book")
    private Book book;
    @SerializedName("reviewNum")
    private int reviewNum;
    @SerializedName("learnNum")
    private int learnNum;
    @SerializedName("learnedNum")
    private LearnedNumBean learnedNum;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getLearnNum() {
        return learnNum;
    }

    public void setLearnNum(int learnNum) {
        this.learnNum = learnNum;
    }

    public LearnedNumBean getLearnedNum() {
        return learnedNum;
    }

    public void setLearnedNum(LearnedNumBean learnedNum) {
        this.learnedNum = learnedNum;
    }

    @Override
    public String toString() {
        return "BookMore{" +
                "book=" + book +
                ", reviewNum=" + reviewNum +
                ", learnNum=" + learnNum +
                ", learnedNum=" + learnedNum +
                '}';
    }
}
