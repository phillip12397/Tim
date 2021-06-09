package com.developfuture.fortknox.spinner;

import com.developfuture.fortknox.R;

public class Transaction {

    private String id;
    private String name;

    public Transaction(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String getId() { return id; }

    public String getName() { return name; }
    public int getImage() {
        switch (getId())
        {
            case "0":
                return R.drawable.ic_home_restaurant;
            case "1":
                return R.drawable.ic_home_games;
            case "2":
                return R.drawable.ic_home_local_gas_station;
            case "3":
                return R.drawable.ic_home_party;
            case "4":
                return R.drawable.ic_home_cake;
            case "5":
                return R.drawable.ic_sport;
        }
        return R.drawable.ic_launcher_background;
    }
}
