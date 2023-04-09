package com.zhaoxiao.zhiying.entity.test;

import com.ahao.basetreeview.model.NodeId;

public class QuestionType implements NodeId {

    private String id;

    private String parentId;

    private String name;


    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPId() {
        return parentId;
    }

    public QuestionType(String name, String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
