package com.developfuture.fortknox;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.home.FinanceTransaction;

import java.util.List;

public class FTViewModel extends AndroidViewModel {
    private FinanceRepository repository;
    private LiveData<List<FinanceTransaction>> allFinances;

    public FTViewModel(@NonNull Application application) {
        super(application);
        repository = new FinanceRepository(application);
        allFinances = repository.getAllFinances();
    }

    public void insert(FinanceTransaction ft){
        repository.insert(ft);
    }

    public void update(FinanceTransaction ft){
        repository.update(ft);
    }

    public void delete(FinanceTransaction ft){
        repository.delete(ft);
    }

    public void deleteAlleFinances(){
        repository.deleteAll();
    }

    public LiveData<List<FinanceTransaction>> getAllFinances() {
        return allFinances;
    }
}
