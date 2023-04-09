package com.zhaoxiao.zhiying.entity.test;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestStype implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("num")
    private int num;
    @SerializedName("finish")
    private int finish;
    @SerializedName("right")
    private int right;

    public TestStype() {
    }

    public TestStype(int id, String name, int num, int finish, int right) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.finish = finish;
        this.right = right;
    }

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Stype{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}