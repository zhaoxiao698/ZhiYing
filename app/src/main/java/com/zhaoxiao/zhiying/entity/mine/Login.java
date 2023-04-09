package com.zhaoxiao.zhiying.entity.mine;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("account")
    private String account;
    @SerializedName("type")
    private LoginType type;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Login{" +
                "account='" + account + '\'' +
                ", type=" + type +
                '}';
    }

    public enum LoginType{
        LOGIN_BY_CODE(1),
        LOGIN_BY_PASSWORD(2),
        REGISTER(3),
        LOGIN_BY_ACCOUNT(4),
        LOGIN_BY_PHONE(5),
        ERROR(6);

        int value;

        LoginType(int value){
            this.value = value;
        }

        static LoginType get(int value){
            switch (value){
                case 1:return LOGIN_BY_CODE;
                case 2:return LOGIN_BY_PASSWORD;
                case 3:return REGISTER;
                case 4:return LOGIN_BY_ACCOUNT;
                case 5:return LOGIN_BY_PHONE;
                default:return ERROR;
            }
        }
    }
}
