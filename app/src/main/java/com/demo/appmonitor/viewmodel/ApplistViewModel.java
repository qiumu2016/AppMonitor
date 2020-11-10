package com.demo.appmonitor.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.appmonitor.model.AppItem;
import com.demo.appmonitor.model.ResearchItem;

import java.util.ArrayList;
import java.util.List;

public class ApplistViewModel extends ViewModel {
    private MutableLiveData<ArrayList<AppItem>> data;
    private Context context;
    List<AppItem> appList = new ArrayList<>();
    public ApplistViewModel() {
        data = new MutableLiveData<>();
    }
    public LiveData<ArrayList<AppItem>> getList() {
        return data;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void flashList(){
        appList.clear();
        //向applist中添加数据
        data.setValue((ArrayList<AppItem>) appList);
    }
}
