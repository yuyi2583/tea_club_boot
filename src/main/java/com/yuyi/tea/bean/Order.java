package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuyi.tea.common.Amount;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class Order implements Serializable {

    private int uid;
    private long orderTime;
    private Customer customer;
    private Clerk clerk;
    private OrderStatus status;
    private List<OrderStatus> orderStatusHistory;
    private List<OrderProduct> products;
    private ActivityRule activityRule;//全局购物满减活动规则
    private List<Activity> activities;
    private Amount amount;
    private String buyerPs;//买家留言
    private String buyerRefundReason;//买家退款理由
    private String sellerPs;//卖家退款说明
    private TrackInfo trackInfo;

    public Order() {
    }

    public Order(int uid) {
        this.uid = uid;
    }

    public Order(int uid, long orderTime, Customer customer, Clerk clerk, OrderStatus status, List<OrderStatus> orderStatusHistory, List<OrderProduct> products, ActivityRule activityRule, List<Activity> activities, Amount amount, String buyerPs, String buyerRefundReason, String sellerPs, TrackInfo trackInfo) {
        this.uid = uid;
        this.orderTime = orderTime;
        this.customer = customer;
        this.clerk = clerk;
        this.status = status;
        this.orderStatusHistory = orderStatusHistory;
        this.products = products;
        this.activityRule = activityRule;
        this.activities = activities;
        this.amount = amount;
        this.buyerPs = buyerPs;
        this.buyerRefundReason = buyerRefundReason;
        this.sellerPs = sellerPs;
        this.trackInfo = trackInfo;
    }

    public String getBuyerRefundReason() {
        return buyerRefundReason;
    }

    public void setBuyerRefundReason(String buyerRefundReason) {
        this.buyerRefundReason = buyerRefundReason;
    }

    public List<OrderStatus> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public void setOrderStatusHistory(List<OrderStatus> orderStatusHistory) {
        this.orderStatusHistory = orderStatusHistory;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public String getBuyerPs() {
        return buyerPs;
    }

    public void setBuyerPs(String buyerPs) {
        this.buyerPs = buyerPs;
    }

    public String getSellerPs() {
        return sellerPs;
    }

    public void setSellerPs(String sellerPs) {
        this.sellerPs = sellerPs;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public ActivityRule getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(ActivityRule activityRule) {
        this.activityRule = activityRule;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Clerk getClerk() {
        return clerk;
    }

    public void setClerk(Clerk clerk) {
        this.clerk = clerk;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uid=" + uid +
                ", orderTime=" + orderTime +
                ", customer=" + customer +
                ", clerk=" + clerk +
                ", status=" + status +
                ", orderStatusHistory=" + orderStatusHistory +
                ", products=" + products +
                ", activityRule=" + activityRule +
                ", activities=" + activities +
                ", amount=" + amount +
                ", buyerPs='" + buyerPs + '\'' +
                ", buyerRefundReason='" + buyerRefundReason + '\'' +
                ", sellerPs='" + sellerPs + '\'' +
                ", trackInfo=" + trackInfo +
                '}';
    }
}
