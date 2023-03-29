package com.zhaoxiao.zhiying.entity;

public class Course {
    private int id;
    private String name;
    private String type;
    private String img;

    public Course() {
    }

    public Course(int id, String name, String type, String img) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.img = img;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
