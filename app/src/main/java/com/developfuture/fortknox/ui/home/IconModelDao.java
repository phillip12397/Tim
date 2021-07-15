package com.developfuture.fortknox.ui.home;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.developfuture.fortknox.ui.investments.Investments;

import java.util.List;

@Dao
public interface IconModelDao {
    @Insert
    void insert(IconModel icon);

    @Update
    void update(IconModel icon);

    @Delete
    void delete(IconModel icon);

    @Query("DELETE FROM icons_table")
    void deleteAllNotes();

    @Query("SELECT * FROM icons_table ORDER BY id")
    LiveData<List<IconModel>> getAllIcons();

}
