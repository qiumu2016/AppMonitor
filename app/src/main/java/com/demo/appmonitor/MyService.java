package com.demo.appmonitor;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.EventStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyService extends Service {
    private Context context = this;
    private MyDatabaseHelper dbHelper;

    public static final String CREATE_PHONE_DATA_LIST = "create table if not exists data_list ("
            + "id integer primary key autoincrement, "
            + "package text,"
            + "stime INTEGER)";

    public static final String CREATE_PHONE_DATA = "create table if not exists data ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "package text,"
            + "last_time INTEGER,"
            + "image text)";

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                Log.i("dong", "开始service");
                Date start_date = null;// 起始时间
                Date end_date;// 结束时间就是现在
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    start_date = df.parse("2020/11/05 00:00:00");
                    end_date = new Date();
                    store_data(start_date.getTime(), end_date.getTime());
                    store_xulie(start_date.getTime(), end_date.getTime());
                    Log.i("dong", "结束一个service");
                } catch (ParseException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Log.i("error", e.getMessage());
                }

            }
        }).start();
        AlarmManager manger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MyReceiver.class);//广播接收
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);//意图为开启广播

        long triggerAtTime = SystemClock.elapsedRealtime();
        triggerAtTime = triggerAtTime + 60*60*1000;//比开机至今的时间增长一个小时
        manger.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        return super.onStartCommand(intent, flags, startId);

    }

    public void store_data(long startTime, long endTime) throws PackageManager.NameNotFoundException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbHelper = new MyDatabaseHelper(context, "phone_data.db", null, 1);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists data");
        db.execSQL(CREATE_PHONE_DATA);

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime, endTime);
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
            try {
                OutputStream os = new FileOutputStream(path);
                img.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
            } catch (Exception e) {
                Log.e("TAG", "", e);
            }
//            String last = dateFormat.format(new Date(stats.getLastTimeUsed()));
            Long last = stats.getLastTimeUsed();
            if (stats.getTotalTimeInForeground() >= 0) {
                values.put("name", name);
                values.put("image", image);
                values.put("package", packageName);
                values.put("last_time", last);
                db.insert("data", null, values);
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void store_xulie(long startTime, long endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dbHelper = new MyDatabaseHelper(context, "phone_data.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("drop table if exists data_list");
        db.execSQL(CREATE_PHONE_DATA_LIST);
        @SuppressLint("WrongConstant") UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        UsageEvents events = mUsmManager.queryEvents(startTime, endTime);

        while (events.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            events.getNextEvent(e);

//            String stime = dateFormat.format(new Date(e.getTimeStamp()));
            Long stime = e.getTimeStamp();
            ContentValues values = new ContentValues();
            if (e != null) {
                values.put("package", e.getPackageName());
                values.put("stime", stime);
                db.insert("data_list", null, values);
            }
        }
    }

}
