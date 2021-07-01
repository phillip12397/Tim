package com.developfuture.fortknox.ui.privacy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrivacyViewModel extends ViewModel{

    private final MutableLiveData<String> mText;

    public PrivacyViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
