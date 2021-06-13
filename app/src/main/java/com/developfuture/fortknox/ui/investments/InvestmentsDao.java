package com.developfuture.fortknox.ui.investments;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InvestmentsDao {

    @Insert
    void insert(Investments invs);

    @Update
    void update(Investments invs);

    @Delete
    void delete(Investments invs);

    @Query("DELETE FROM investments_table")
    void deleteAllNotes();

    @Query("SELECT * FROM investments_table ORDER BY id")
    LiveData<List<Investments>> getAllInvestments();
}
