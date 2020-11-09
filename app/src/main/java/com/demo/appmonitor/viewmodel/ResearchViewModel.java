package com.demo.appmonitor.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.appmonitor.model.ResearchItem;

import java.util.ArrayList;
import java.util.List;

public class ResearchViewModel  extends ViewModel {
    private MutableLiveData<ArrayList<ResearchItem>> data;
    private Context context;
    List<ResearchItem> appList = new ArrayList<>();
    public ResearchViewModel() {
        data = new MutableLiveData<>();
    }
    public LiveData<ArrayList<ResearchItem>> getList() {
        return data;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void flashList(){
        appList.clear();
        //向applist中添加数据
        data.setValue((ArrayList<ResearchItem>) appList);
    }
}
