package com.developfuture.fortknox;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FinanceRepository {
    private FinanceTransactionDao ftDao;
    private LiveData<List<FinanceTransaction>> allFinances;

    public FinanceRepository(Application application){
        FinanceTransactionDatabase db = FinanceTransactionDatabase.getInstance(application);
        ftDao = db.transDao();
        allFinances = ftDao.getAllFinances();
    }

    public void insert(FinanceTransaction ft){
        new InsertAsyncTask(ftDao).execute(ft);
    }

    public void update(FinanceTransaction ft){
        new UpdateAsyncTask(ftDao).execute(ft);
    }

    public void delete(FinanceTransaction ft){
        new DeleteAsyncTask(ftDao).execute(ft);
    }

    public void deleteAll(){
        new DeleteAllNoteAsyncTask(ftDao).execute();
    }

    public LiveData<List<FinanceTransaction>> getAllFinances() {
        return allFinances;
    }

    private static class InsertAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private FinanceTransactionDao ftDao;

        private InsertAsyncTask(FinanceTransactionDao ftDao){
            this.ftDao = ftDao;
        }

        @Override
        protected Void doInBackground(FinanceTransaction... financeTransactions) {
            ftDao.insert(financeTransactions[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private FinanceTransactionDao ftDao;

        private UpdateAsyncTask(FinanceTransactionDao ftDao){
            this.ftDao = ftDao;
        }

        @Override
        protected Void doInBackground(FinanceTransaction... financeTransactions) {
            ftDao.update(financeTransactions[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private FinanceTransactionDao ftDao;

        private DeleteAsyncTask(FinanceTransactionDao ftDao){
            this.ftDao = ftDao;
        }

        @Override
        protected Void doInBackground(FinanceTransaction... financeTransactions) {
            ftDao.delete(financeTransactions[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private FinanceTransactionDao ftDao;

        private DeleteAllNoteAsyncTask(FinanceTransactionDao ftDao){
            this.ftDao = ftDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ftDao.deleteAllNotes();
            return null;
        }
    }

}
