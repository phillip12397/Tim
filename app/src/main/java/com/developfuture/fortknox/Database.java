package com.developfuture.fortknox;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.developfuture.fortknox.ui.home.FinanceTransactionDao;
import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsDao;
import com.developfuture.fortknox.ui.investments.InvestmentsHistory;
import com.developfuture.fortknox.ui.investments.InvestmentsHistoryDao;

@androidx.room.Database(entities = {FinanceTransaction.class, Investments.class, InvestmentsHistory.class}, version = 9)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract FinanceTransactionDao transDao();
    public abstract InvestmentsDao iDao();
    public abstract InvestmentsHistoryDao ihDao();

    public static synchronized Database getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "finance_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}