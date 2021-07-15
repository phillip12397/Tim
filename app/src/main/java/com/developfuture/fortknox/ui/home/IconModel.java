package com.developfuture.fortknox.ui.home;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Entity(tableName = "icons_table")
public class IconModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int viewId;
    private int referenceId;

    public IconModel(int viewId, int referenceId) {
        this.viewId = viewId;
        this.referenceId = referenceId;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
