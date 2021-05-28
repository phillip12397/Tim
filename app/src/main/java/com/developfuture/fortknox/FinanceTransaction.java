package com.developfuture.fortknox;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "finance_table")
public class FinanceTransaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String date;
    private String price;

    public FinanceTransaction(String name, String date, String price) {
        this.name = name;
        this.date = date;
        this.price = price;
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

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
