package com.developfuture.fortknox;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.developfuture.fortknox.ui.home.FinanceTransactionDao;
import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsDao;
import com.developfuture.fortknox.ui.investments.InvestmentsHistory;
import com.developfuture.fortknox.ui.investments.InvestmentsHistoryDao;

import java.util.List;

public class Repository {
    private final FinanceTransactionDao ftDao;
    private final InvestmentsDao iDao;
    private final InvestmentsHistoryDao ihDao;
    private final LiveData<List<FinanceTransaction>> allFinances;
    private final LiveData<List<Investments>> allInvestments;
    private final LiveData<List<InvestmentsHistory>> allInvestmentsHistory;

    public Repository(Application application){
        Database db = Database.getInstance(application);
        ftDao = db.transDao();
        iDao = db.iDao();
        ihDao = db.ihDao();
        allFinances = ftDao.getAllFinances();
        allInvestments = iDao.getAllInvestments();
        allInvestmentsHistory = ihDao.getAllInvestmentsHistory();
    }

    public void insert(FinanceTransaction ft){
        new InsertAsyncTask(ftDao).execute(ft);
    }

    public void insert(Investments inv){
        new InsertAsyncTaskI(iDao).execute(inv);
    }

    public void insert(InvestmentsHistory ih){
        new InsertAsyncTaskIH(ihDao).execute(ih);
    }

    public void update(FinanceTransaction ft){
        new UpdateAsyncTask(ftDao).execute(ft);
    }

    public void update(Investments inv){
        new UpdateAsyncTaskI(iDao).execute(inv);
    }

    public void update(InvestmentsHistory ih){
        new UpdateAsyncTaskIH(ihDao).execute(ih);
    }

    public void delete(FinanceTransaction ft){
        new DeleteAsyncTask(ftDao).execute(ft);
    }

    public void delete(Investments inv){
        new DeleteAsyncTaskI(iDao).execute(inv);
    }

    public void delete(InvestmentsHistory ih){
        new DeleteAsyncTaskIH(ihDao).execute(ih);
    }

    public void deleteAll(){
        new DeleteAllNoteAsyncTask(ftDao).execute();
    }

    public void deleteAllI(){
        new DeleteAllNoteAsyncTaskI(iDao).execute();
    }

    public void deleteAllIH(){
        new DeleteAllNoteAsyncTaskIH(ihDao).execute();
    }

    public LiveData<List<FinanceTransaction>> getAllFinances() {
        return allFinances;
    }

    public LiveData<List<Investments>> getAllInvestments() {
        return allInvestments;
    }

    public LiveData<List<InvestmentsHistory>> getAllInvestmentsHistory() {
        return allInvestmentsHistory;
    }

    private static class InsertAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private final FinanceTransactionDao ftDao;

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
        private final InvestmentsDao iDao;

        private InsertAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.insert(investments[0]);
            return null;
        }
    }

    private static class InsertAsyncTaskIH extends AsyncTask<InvestmentsHistory, Void, Void> {
        private final InvestmentsHistoryDao ihDao;

        private InsertAsyncTaskIH(InvestmentsHistoryDao ihDao){
            this.ihDao = ihDao;
        }

        @Override
        protected Void doInBackground(InvestmentsHistory... investmentsHistories) {
            ihDao.insert(investmentsHistories[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private final FinanceTransactionDao ftDao;

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
        private final InvestmentsDao iDao;

        private UpdateAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.update(investments[0]);
            return null;
        }
    }

    private static class UpdateAsyncTaskIH extends AsyncTask<InvestmentsHistory, Void, Void> {
        private final InvestmentsHistoryDao ihDao;

        private UpdateAsyncTaskIH(InvestmentsHistoryDao ihDao){
            this.ihDao = ihDao;
        }

        @Override
        protected Void doInBackground(InvestmentsHistory... investmentsHistories) {
            ihDao.update(investmentsHistories[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<FinanceTransaction, Void, Void> {
        private final FinanceTransactionDao ftDao;

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
        private final InvestmentsDao iDao;

        private DeleteAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Investments... investments) {
            iDao.delete(investments[0]);
            return null;
        }
    }

    private static class DeleteAsyncTaskIH extends AsyncTask<InvestmentsHistory, Void, Void> {
        private final InvestmentsHistoryDao ihDao;

        private DeleteAsyncTaskIH(InvestmentsHistoryDao ihDao){
            this.ihDao = ihDao;
        }

        @Override
        protected Void doInBackground(InvestmentsHistory... investmentsHistories) {
            ihDao.delete(investmentsHistories[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private final FinanceTransactionDao ftDao;

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
        private final InvestmentsDao iDao;

        private DeleteAllNoteAsyncTaskI(InvestmentsDao iDao){
            this.iDao = iDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            iDao.deleteAllNotes();
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTaskIH extends AsyncTask<Void, Void, Void> {
        private final InvestmentsHistoryDao ihDao;

        private DeleteAllNoteAsyncTaskIH(InvestmentsHistoryDao ihDao){
            this.ihDao = ihDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ihDao.deleteAllInvestmentsHistory();
            return null;
        }
    }
}
