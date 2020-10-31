package com.demo.appmonitor.ui.applist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AppListViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is applist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}