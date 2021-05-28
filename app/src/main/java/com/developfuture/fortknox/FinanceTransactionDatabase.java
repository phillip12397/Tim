package com.developfuture.fortknox;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FinanceTransaction.class}, version = 1)
public abstract class FinanceTransactionDatabase extends RoomDatabase {

    private static FinanceTransactionDatabase instance;

    public abstract FinanceTransactionDao transDao();

    public static synchronized FinanceTransactionDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FinanceTransactionDatabase.class, "finance_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FinanceTransactionDao ftDao;
        private PopulateDbAsyncTask(FinanceTransactionDatabase db) {
            ftDao = db.transDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ftDao.insert(new FinanceTransaction("Einkaufen", "30.04.2019", "35$"));
            return null;
        }
    }
}
