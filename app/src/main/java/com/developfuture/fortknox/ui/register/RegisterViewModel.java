package com.developfuture.fortknox.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegisterViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is the login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
