package com.demo.appmonitor.ui.research;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ResearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is research fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}