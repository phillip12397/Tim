package com.developfuture.fortknox;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.developfuture.fortknox.ui.home.FinanceTransactionDao;
import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsDao;

import java.util.List;

public class Repository {
    private FinanceTransactionDao ftDao;
    private InvestmentsDao iDao;
    private LiveData<List<FinanceTransaction>> allFinances;
    private LiveData<List<Investments>> allInvestments;

    public Repository(Application application){
        Database db = Database.getInstance(application);
        ftDao = db.transDao();
        iDao = db.iDao();
        allFinances = ftDao.getAllFinances();
        allInvestments = iDao.getAllInvestments();
    }

    public void insert(FinanceTransaction ft){
        new InsertAsyncTask(ftDao).execute(ft);
    }

    public void insert(Investments inv){
        new InsertAsyncTaskI(iDao).execute(inv);
    }

    public void update(FinanceTransaction ft){
        new UpdateAsyncTask(ftDao).execute(ft);
    }

    public void update(Investments inv){
        new UpdateAsyncTaskI(iDao).execute(inv);
    }

    public void delete(FinanceTransaction ft){
        new DeleteAsyncTask(ftDao).execute(ft);
    }

    public void delete(Investments inv){
        new DeleteAsyncTaskI(iDao).execute(inv);
    }

    public void deleteAll(){
        new DeleteAllNoteAsyncTask(ftDao).execute();
    }

    public void deleteAllI(){
        new DeleteAllNoteAsyncTaskI(iDao).execute();
    }

    public LiveData<List<FinanceTransaction>> getAllFinances() {
        return allFinances;
    }

    public LiveData<List<Investments>> getAllInvestments() {
        return allInvestments;
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

    private static class InsertAsyncTaskI extends AsyncTask<Investments, Void, Void> {
        private InvestmentsDao iDao;

        private InsertAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.insert(investments[0]);
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

    private static class UpdateAsyncTaskI extends AsyncTask<Investments, Void, Void> {
        private InvestmentsDao iDao;

        private UpdateAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.update(investments[0]);
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

    private static class DeleteAsyncTaskI extends AsyncTask<Investments, Void, Void> {
        private InvestmentsDao iDao;

        private DeleteAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.delete(investments[0]);
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

    private static class DeleteAllNoteAsyncTaskI extends AsyncTask<Void, Void, Void> {
        private InvestmentsDao iDao;

        private DeleteAllNoteAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            iDao.deleteAllNotes();
            return null;
        }
    }
}
