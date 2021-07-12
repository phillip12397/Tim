package com.developfuture.fortknox.ui.home;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "finance_table")
public class FinanceTransaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String name;
    private final String date;
    private final String price;
    private String userUID;

    public FinanceTransaction(String name, String date, String price, String userUID) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.userUID = userUID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserUID(){
        return userUID;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
