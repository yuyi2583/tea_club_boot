package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * 门店营业时间类
 */
@JsonIgnoreProperties(value = { "handler" })
public class OpenHour implements Serializable {

    private int uid;
    private String startTime;
    private String endTime;
    private List<String> date;
    private int shopId;


    public OpenHour() {
    }

    public OpenHour(int uid, String startTime, String endTime, List<String> date, int shopId) {
        this.uid = uid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "OpenHour{" +
                "uid=" + uid +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", date=" + date +
                ", shopId=" + shopId +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
