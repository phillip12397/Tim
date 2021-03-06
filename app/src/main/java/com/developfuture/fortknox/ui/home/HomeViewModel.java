package com.developfuture.fortknox.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Finanzes");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void setText(String mText) {
        this.mText.setValue(mText);
    }
}