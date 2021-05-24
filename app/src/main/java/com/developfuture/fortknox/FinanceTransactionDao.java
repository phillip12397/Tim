package com.developfuture.fortknox;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FinanceTransactionDao {


    @Insert
    void insert(FinanceTransaction trans);

    @Update
    void update(FinanceTransaction note);

    @Delete
    void delete(FinanceTransaction note);

    @Query("DELETE FROM finance_table")
    void deleteAllNotes();

    @Query("SELECT * FROM finance_table ORDER BY id")
    LiveData<List<FinanceTransaction>> getAllFinances();

}
