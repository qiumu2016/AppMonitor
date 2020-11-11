package com.demo.appmonitor.viewmodel;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.appmonitor.MainActivity;
import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.model.ResearchItem;

import java.util.ArrayList;
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

    public void flashList() throws PackageManager.NameNotFoundException {
        appList.clear();
        //向applist中添加数据
        appList = getData();
        Log.i("dong", String.valueOf(appList.size()));
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.setValue((ArrayList<ResearchItem>) appList);
            }
        });
        //data.setValue((ArrayList<ResearchItem>) appList);
    }

    public List<ResearchItem> getData() throws PackageManager.NameNotFoundException {
        List<ResearchItem> lists = new ArrayList<>();
        dbHelper = new MyDatabaseHelper(context, "phone_data.db", null, 1);
        Log.i("dong","getData 进来了");

        // orderBy加了个DESC，按last_time的倒序查询
        Cursor cursor = dbHelper.getReadableDatabase().query("data", null, null, null, null, null, "last_time DESC");
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String packageName = cursor.getString(cursor.getColumnIndex("package"));
                Bitmap bitmap = BitmapFactory.decodeFile(image);
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                ResearchItem item = new ResearchItem();
                item.setImageId(image);
                item.setPackageName(packageName);
                item.setLastTime(last_time);
                item.setAppName(name);
                lists.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.i("dong","getData 获得到东西结束了");
        return lists;
    }

}
