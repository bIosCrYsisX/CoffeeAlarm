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

    @ColumnInfo(name = "start")
    private String start;

    @ColumnInfo(name = "end")
    private String end;

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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
