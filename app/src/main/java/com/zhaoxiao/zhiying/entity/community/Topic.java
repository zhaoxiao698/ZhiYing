package com.zhaoxiao.zhiying.entity.community;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Topic implements Serializable {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("info")
        private String info;
        @SerializedName("addTime")
        private Date addTime;
        @SerializedName("userAccount")
        private String userAccount;
        @SerializedName("join")
        private int join;
        @SerializedName("collection")
        private int collection;
        private boolean collectStatus;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Date getAddTime() {
            return addTime;
        }

        public void setAddTime(Date addTime) {
            this.addTime = addTime;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public int getJoin() {
            return join;
        }

        public void setJoin(int join) {
            this.join = join;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public boolean getCollectStatus() {
            return collectStatus;
        }

        public void setCollectStatus(boolean collectStatus) {
            this.collectStatus = collectStatus;
        }

        @Override
        public String toString() {
            return "Topic{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", info='" + info + '\'' +
                    ", addTime=" + addTime +
                    ", userAccount='" + userAccount + '\'' +
                    ", join=" + join +
                    ", collection=" + collection +
                    ", collectStatus=" + collectStatus +
                    '}';
        }
}
