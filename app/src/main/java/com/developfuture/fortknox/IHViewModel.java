package com.developfuture.fortknox;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsHistory;

import java.util.List;

public class IHViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<InvestmentsHistory>> allInvestmentsHistory;

    public IHViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allInvestmentsHistory = repository.getAllInvestmentsHistory();
    }

    public void insert(InvestmentsHistory ihs){
        repository.insert(ihs);
    }

    public void update(InvestmentsHistory ihs){
        repository.update(ihs);
    }

    public void delete(InvestmentsHistory ihs){
        repository.delete(ihs);
    }

    public void deleteAllInvestmentsHistory(){
        repository.deleteAllIH();
    }

    public LiveData<List<InvestmentsHistory>> getAllInvestmentsHistory(){
        return allInvestmentsHistory;
    }
}
