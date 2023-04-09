package com.zhaoxiao.zhiying.entity.test;

public class Label{
        private int questionBankId;
        private String questionBank;

        public Label() {
        }

        public Label(int questionBankId, String questionBank) {
            this.questionBankId = questionBankId;
            this.questionBank = questionBank;
        }

        public int getQuestionBankId() {
            return questionBankId;
        }

        public void setQuestionBankId(int questionBankId) {
            this.questionBankId = questionBankId;
        }

        public String getQuestionBank() {
            return questionBank;
        }

        public void setQuestionBank(String questionBank) {
            this.questionBank = questionBank;
        }
    }