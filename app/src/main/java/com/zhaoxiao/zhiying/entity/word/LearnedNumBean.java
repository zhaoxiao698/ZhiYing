package com.zhaoxiao.zhiying.entity.word;

import com.google.gson.annotations.SerializedName;

public class LearnedNumBean {
        @SerializedName("day")
        private String day;
        @SerializedName("num")
        private int num;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

    @Override
    public String toString() {
        return "LearnedNumBean{" +
                "day='" + day + '\'' +
                ", num=" + num +
                '}';
    }
}
