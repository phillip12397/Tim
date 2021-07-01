package com.developfuture.fortknox.ui.legalNotification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LegalNotificationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LegalNotificationViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
