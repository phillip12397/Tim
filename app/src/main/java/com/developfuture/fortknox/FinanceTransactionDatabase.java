package com.developfuture.fortknox;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.developfuture.fortknox.ui.home.FinanceTransactionDao;
import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsDao;

@Database(entities = {FinanceTransaction.class, Investments.class}, version = 2)
public abstract class FinanceTransactionDatabase extends RoomDatabase {

 //   public abstract FinanceTransactionDao ftDao();
 //   public abstract InvestmentsDao iDao();

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