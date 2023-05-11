package com.zhaoxiao.zhiying.entity.mine;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CalendarInfo implements Serializable {
    @SerializedName("continuous")
    private int continuous;
    @SerializedName("total")
    private int total;
    @SerializedName("planList")
    private List<Plan> planList;

    public CalendarInfo() {
    }

    public CalendarInfo(int continuous, int total, List<Plan> planList) {
        this.continuous = continuous;
        this.total = total;
        this.planList = planList;
    }

    public int getContinuous() {
        return continuous;
    }

    public void setContinuous(int continuous) {
        this.continuous = continuous;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }
}
