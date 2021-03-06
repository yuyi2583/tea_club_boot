package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = { "handler" })
public class OrderStatus implements Serializable {

    private int uid;
    private int orderId;
    private String status;
    private long time;

    public OrderStatus() {
    }

    public OrderStatus(int uid) {
        this.uid = uid;
    }

    public OrderStatus(int uid, int orderId, String status, long time) {
        this.uid = uid;
        this.orderId = orderId;
        this.status = status;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "uid=" + uid +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                ", time=" + time +
                '}';
    }
}
