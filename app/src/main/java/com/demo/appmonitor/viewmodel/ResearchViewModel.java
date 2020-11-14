package com.demo.appmonitor.viewmodel;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.appmonitor.MainActivity;
import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.model.ResearchItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResearchViewModel  extends ViewModel {

    private MutableLiveData<ArrayList<ResearchItem>> data;

    private Context context;

    private MyDatabaseHelper dbHelper;
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

    public void flashList(String date) throws PackageManager.NameNotFoundException, ParseException {
        appList.clear();

        //向appList中添加数据L
        appList = getData(date);
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.setValue((ArrayList<ResearchItem>) appList);
            }
        });
    }


    public List<ResearchItem> getData(String lastTime) throws PackageManager.NameNotFoundException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date lastDate = df.parse(lastTime);
        List<ResearchItem> lists = new ArrayList<>();
        dbHelper = new MyDatabaseHelper(context, "phone_data.db", null, 1);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM data WHERE "+
                "last_time <= ?",
        new String[] { String.valueOf(lastDate.getTime())});
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Long last_time = cursor.getLong(cursor.getColumnIndex("last_time"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String packageName = cursor.getString(cursor.getColumnIndex("package"));
                ResearchItem item = new ResearchItem();
                item.setImageId(image);
                item.setPackageName(packageName);
                item.setLastTime(df.format(last_time));
                item.setAppName(name);
                lists.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lists;
    }

}
