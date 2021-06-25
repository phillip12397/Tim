package com.developfuture.fortknox.spinner;

import com.developfuture.fortknox.R;

import java.util.ArrayList;

public class TransaktionTypes {

    private static final ArrayList<Transaction> transactionTypesArrayList = new ArrayList<>();

    private String id;
    private String name;

    public static ArrayList<Transaction> getTransactionTypesArrayList() {
        return transactionTypesArrayList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameById(int id) {
        Transaction current = transactionTypesArrayList.get(id);
        return current.getName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransaktionTypes() {
        initTransactionType();
    }

    public void initTransactionType() {
        if(transactionTypesArrayList != null) {
            transactionTypesArrayList.clear();
        }

        Transaction transaction1 = new Transaction("0", "Food");
        Transaction transaction2 = new Transaction("1", "Games");
        Transaction transaction3 = new Transaction("2", "Gas Station");
        Transaction transaction4 = new Transaction("3", "Party");
        Transaction transaction5 = new Transaction("4", "Birthday");
        Transaction transaction6 = new Transaction("5", "Sport");

        transactionTypesArrayList.add(transaction1);
        transactionTypesArrayList.add(transaction2);
        transactionTypesArrayList.add(transaction3);
        transactionTypesArrayList.add(transaction4);
        transactionTypesArrayList.add(transaction5);
        transactionTypesArrayList.add(transaction6);
    }
}
