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

@androidx.room.Database(entities = {FinanceTransaction.class, Investments.class}, version = 3)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract FinanceTransactionDao transDao();
    public abstract InvestmentsDao iDao();

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
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final FinanceTransactionDao ftDao;

        private PopulateDbAsyncTask(Database db) {
            ftDao = db.transDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ftDao.insert(new FinanceTransaction("Einkaufen", "30.04.2019", "35$"));
            return null;
        }
    }
}