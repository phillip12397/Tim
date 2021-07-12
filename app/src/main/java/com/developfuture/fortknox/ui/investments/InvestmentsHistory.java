package com.developfuture.fortknox.ui.investments;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "investments_history_table")
public class InvestmentsHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String asset;
    private double stock;
    private double price;
    private int sellOrBuy;
    private String userUID;

    public InvestmentsHistory(String asset, double stock, double price, int sellOrBuy, String userUID) {
        this.asset = asset;
        this.stock = stock;
        this.price = price;
        this.sellOrBuy = sellOrBuy;
        this.userUID = userUID;
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

    public String getUserUID(){
        return userUID;
    }

    public int getSellOrBuy() {
        return sellOrBuy;
    }

    public double getPrice() {
        return price;
    }

}
