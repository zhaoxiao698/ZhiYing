package com.zhaoxiao.zhiying.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Ftype implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("stypeList")
    private List<Stype> stypeList;

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

    public List<Stype> getStypeList() {
        return stypeList;
    }

    public void setStypeList(List<Stype> stypeList) {
        this.stypeList = stypeList;
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
