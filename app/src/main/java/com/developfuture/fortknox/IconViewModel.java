package com.developfuture.fortknox;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.developfuture.fortknox.ui.home.IconModel;
import com.developfuture.fortknox.ui.investments.InvestmentsHistory;

import java.util.List;

public class IconViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<IconModel>> allicons;

    public IconViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allicons = repository.getAllIconModels();
    }

    public void insert(IconModel ihs){
        repository.insert(ihs);
    }

    public void update(IconModel ihs){
        repository.update(ihs);
    }

    public void delete(IconModel ihs){
        repository.delete(ihs);
    }

    public void deleteAllIconModels(){
        repository.deleteAllIconModels();
    }

    public LiveData<List<IconModel>> getAllIconModels(){
        return allicons;
    }
}
