package com.zhaoxiao.zhiying.entity.study;

import com.google.gson.annotations.SerializedName;

public class Sentence {
        @SerializedName("id")
        private int id;
        @SerializedName("articleId")
        private int articleId;
        @SerializedName("order")
        private int order;
        @SerializedName("english")
        private String english;
        @SerializedName("translation")
        private String translation;
        @SerializedName("node")
        private int node;
        @SerializedName("first")
        private int first;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }

        public int getNode() {
            return node;
        }

        public void setNode(int node) {
            this.node = node;
        }

        public int getFirst() {
            return first;
        }

        public void setFirst(int first) {
            this.first = first;
        }
    }
