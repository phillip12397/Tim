package com.developfuture.fortknox.ui.investments;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "investments_table")
public class Investments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String asset;
    private final String stock;
    private final String price;

    public Investments(String asset, String stock, String price) {
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

    public String getStock() {
        return stock;
    }

    public String getPrice() {
        return price;
    }
}
