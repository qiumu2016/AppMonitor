package com.demo.appmonitor.viewmodel;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.appmonitor.MainActivity;
import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.MyReceiver;
import com.demo.appmonitor.MyService;
import com.demo.appmonitor.model.ResearchItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchViewModel  extends ViewModel {

    private MutableLiveData<ArrayList<ResearchItem>> data;
    public Map<String, Map<String, Integer>> list;

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
        appList = getData2(date);
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.setValue((ArrayList<ResearchItem>) appList);
            }
        });
    }

    public List<ResearchItem> getData2(String lastTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startTime = df.parse("2020/11/05 00:00:00");
        Date endTime = df.parse(lastTime);
        List<ResearchItem> lists = new ArrayList<>();
        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime.getTime(), endTime.getTime());
        for (Map.Entry<String, UsageStats> entry : map.entrySet()) {
            UsageStats stats = entry.getValue();
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = null;
            if (stats.getPackageName()==null){
                continue;
            }
            try {
                applicationInfo = pm.getApplicationInfo(stats.getPackageName(), PackageManager.GET_META_DATA);
            }catch (Exception e) {

            }
            if (applicationInfo==null){
                continue;
            }
            ContentValues values = new ContentValues();
            // 获取应用名称
            String name = (String) pm.getApplicationLabel(applicationInfo);
            String packageName = applicationInfo.packageName;
            // 获取应用图标
            Drawable icon = applicationInfo.loadIcon(context.getPackageManager());
            Bitmap img = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && icon instanceof AdaptiveIconDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(
                        icon.getIntrinsicWidth(),
                        icon.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888
                );
                Canvas canvas = new Canvas(bitmap);
                icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                icon.draw(canvas);
                img = bitmap;
            } else {
                // 有些应用好像并没有图片，VectorDrawable 不能转 BitmapDrawable
                try {
                    BitmapDrawable d = (BitmapDrawable) icon;
                    img = d.getBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String fn = name + ".png";
            String path = context.getFilesDir() + File.separator + fn;
            String image = path;

            OutputStream os = null;
            try {
                os = new FileOutputStream(path);
                img.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Long last = stats.getLastTimeUsed();
            if (last < df.parse("2020/01/01 00:00:00").getTime()) {
                continue;
            }
            if (stats.getTotalTimeInForeground() >= 0) {

                ResearchItem item = new ResearchItem();
                item.setImageId(image);
                item.setPackageName(packageName);
                item.setLastTime(df.format(last));
                item.setAppName(name);
                lists.add(item);
            }
        }
        return lists;
    }


}
