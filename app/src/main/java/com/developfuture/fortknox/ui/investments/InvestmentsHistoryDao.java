package com.developfuture.fortknox.ui.investments;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface InvestmentsHistoryDao {

    @Insert
    void insert(InvestmentsHistory invs);

    @Update
    void update(InvestmentsHistory invs);

    @Delete
    void delete(InvestmentsHistory invs);

    @Query("DELETE FROM investments_history_table")
    void deleteAllInvestmentsHistory();

    @Query("SELECT * FROM investments_history_table ORDER BY id")
    LiveData<List<InvestmentsHistory>> getAllInvestmentsHistory();
}
