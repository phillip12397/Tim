package com.developfuture.fortknox;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.investments.Investments;

import java.util.List;

public class IViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Investments>> allInvestments;

    public IViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allInvestments = repository.getAllInvestments();
    }

    public void insert(Investments inv){
        repository.insert(inv);
    }

    public void update(Investments inv){
        repository.update(inv);
    }

    public void delete(Investments inv){
        repository.delete(inv);
    }

    public void deleteAllInvestments(){
        repository.deleteAllI();
    }

    public LiveData<List<Investments>> getAllInvestments(){
        return allInvestments;
    }
}
