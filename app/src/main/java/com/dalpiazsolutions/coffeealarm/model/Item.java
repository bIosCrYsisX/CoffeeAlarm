package com.dalpiazsolutions.coffeealarm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prices")
public class Item {
    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "shop")
    private String shop;

    @ColumnInfo(name = "startDate")
    private String startDate;

    @ColumnInfo(name = "startTime")
    private String startTime;

    @ColumnInfo(name = "endDate")
    private String endDate;

    @ColumnInfo(name = "endTime")
    private String endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
