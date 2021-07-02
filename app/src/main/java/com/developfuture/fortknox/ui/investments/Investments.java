package com.developfuture.fortknox.ui.investments;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "investments_table")
public class Investments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String asset;
    private double stock;
    private double price;

    public Investments(String asset, double stock, double price) {
        this.asset = asset;
        this.stock = stock;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAsset() {
        return asset;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock){
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}
