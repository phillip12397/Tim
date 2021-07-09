package com.developfuture.fortknox.spinner;

import java.util.ArrayList;

public class InvestmentTypes {

    private static final ArrayList<Investments> investmentTypesArrayList = new ArrayList<>();

    private String id;
    private String name;

    public static ArrayList<Investments> getInvestmantTypesArrayList() {
        return investmentTypesArrayList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameById(int id) {
        Investments current = investmentTypesArrayList.get(id);
        return current.getName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InvestmentTypes() {
        initInvestmentType();
    }

    public void initInvestmentType() {
        if(investmentTypesArrayList != null) {
            investmentTypesArrayList.clear();
        }

        Investments Investments1 = new Investments("0", "Btc");
        Investments investments2 = new Investments("1", "Eth");
        Investments investments3 = new Investments("2", "Matic");
        Investments investments4 = new Investments("3", "Doge");

        investmentTypesArrayList.add(Investments1);
        investmentTypesArrayList.add(investments2);
        investmentTypesArrayList.add(investments3);
        investmentTypesArrayList.add(investments4);
    }
}
