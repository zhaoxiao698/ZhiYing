package com.zhaoxiao.zhiying.entity.test;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TestFtype implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("stypeList")
    private List<TestStype> stypeList;
    @SerializedName("isExpand")
    private boolean isExpand;
    @SerializedName("num")
    private int num;
    @SerializedName("finish")
    private int finish;
    @SerializedName("right")
    private int right;

    public TestFtype() {
    }

    public TestFtype(int id, String name, List<TestStype> stypeList, boolean isExpand, int num, int finish, int right) {
        this.id = id;
        this.name = name;
        this.stypeList = stypeList;
        this.isExpand = isExpand;
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

    public List<TestStype> getStypeList() {
        return stypeList;
    }

    public void setStypeList(List<TestStype> stypeList) {
        this.stypeList = stypeList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
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

    public String stypeToString() {
        String str = "";
        for (TestStype stype : stypeList) {
            str+=stype.getName()+'、';
        }
        return str.substring(0,str.length()-1);
    }

    @Override
    public String toString() {
        return "Ftype{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stypeList=" + stypeList +
                '}';
    }
}
