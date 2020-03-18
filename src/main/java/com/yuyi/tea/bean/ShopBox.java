package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = { "handler" })
public class ShopBox implements Serializable {

    private int uid;
    private String name;
    private String description;
    private int shopId;
    private String boxNum;
    private float price;
    private List<Photo> photos;
    private int duration;//每泡茶时间

    public ShopBox() {
    }

    public ShopBox(int uid, String name, String description, int shopId, String boxNum, float price, List<Photo> photos, int duration) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.shopId = shopId;
        this.boxNum = boxNum;
        this.price = price;
        this.photos = photos;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ShopBox{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shopId=" + shopId +
                ", boxNum='" + boxNum + '\'' +
                ", price=" + price +
                ", photos=" + photos +
                ", duration=" + duration +
                '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}