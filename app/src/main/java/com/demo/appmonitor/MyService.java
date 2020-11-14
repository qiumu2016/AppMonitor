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
import android.database.Cursor;
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

import com.demo.appmonitor.model.ResearchItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
                    store_xulie(start_date.getTime(), end_date.getTime());
                    Log.i("dong", "结束一个service");
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {

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

            Long stime = e.getTimeStamp();
            ContentValues values = new ContentValues();
            if (e != null) {
                values.put("package", e.getPackageName());
                values.put("stime", stime);
                db.insert("data_list", null, values);
            }
        }
        db.close();
        dbHelper.close();
    }

}
