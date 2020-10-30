package com.demo.appmonitor.ui.research;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.USAGE_STATS_SERVICE;

public class ResearchFragment extends Fragment {

    private ResearchViewModel researchViewModel;
    private View mView;
    private ListView lView;
    private SimpleAdapter mAdapter;
    private ArrayList<Map<String, Object>> lists;
    private static MyDatabaseHelper dbHelper;
    public static final String CREATE_PHONE_DATA = "create table if not exists data ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "package text,"
            + "last_time text,"
            + "image text)";
    String start_date_string = "2020/10/29 00:00:00";
    Date start_date;// 起始时间

    Date end_date;// 结束时间就是现在
    String end_date_string;
    SimpleDateFormat df;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        researchViewModel =
                ViewModelProviders.of(this).get(ResearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_research, container, false);
        mView = root;
        lView = root.findViewById(R.id.ll1);

        // lists 是 放入到listView 中的数据
        lists = new ArrayList<Map<String, Object>>();

        lists = getData();

        mAdapter = new SimpleAdapter(root.getContext(), lists, R.layout.list_item
                , new String[]{"image", "name", "last_time","package"}
                , new int[]{R.id.image1, R.id.text1, R.id.text2,R.id.text3});

        // 这个不加 图片显示不出来
        mAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else {
                    return false;
                }
            }
        });

        lView.setAdapter(mAdapter);
        // 获取时间序列的函数
        // 输出结果如下:
        /**
         * 启动时间               应用的包名
         * 2020-10-28 04:43:36   event:com.example.demo
         * 2020-10-28 04:43:36   event:com.google.android.apps.nexuslauncher
         * 2020-10-28 04:43:36   event:com.google.android.apps.nexuslauncher
         * 2020-10-28 04:43:37   event:com.example.demo
         * 2020-10-28 04:43:54   event:com.example.demo
         * 2020-10-28 04:43:54   event:com.google.android.apps.nexuslauncher
         * 2020-10-28 04:43:55   event:com.google.android.apps.nexuslauncher
         * 2020-10-28 04:43:55   event:com.example.demo
         * 2020-10-28 04:43:58   event:com.example.demo
         * 2020-10-28 04:43:58   event:com.example.demo
         */
        Log.i("time", end_date_string);
        ArrayList<ArrayList<String>> lists = null;

        lists = getEventList(mView.getContext(), start_date.getTime(), end_date.getTime());

        Log.i("tip", "清洗前的大小" + lists.size());


        for (int i = 0; i < lists.size(); i++) {
            int j = i + 1;
            while (j < lists.size() && (lists.get(i).get(0) == (lists.get(j).get(0)))) {
                lists.remove(j);
                j = i + 1;
            }
        }

        Log.i("tip", "清洗后的大小" + lists.size());

        // 当其实时间设置为今天零点
        // maps 获得到的是某个应用 在具体的某个时间段的使用与否
        // 例如
        /**
         * key:com.sina.weibo val:{07=1, 08=8}
         * key:com.example.demo val:{11=26, 13=5}
         * 表示 微博 在 07点 使用1次  08点使用8次
         * demo 在 11点(26次) 13点(5次)
         */

        final Map<String, Map<String, Integer> > maps = new HashMap<>();
        for (int i = 0; i < lists.size(); i++) {
            if (maps.containsKey(lists.get(i).get(0))){
                String temp = lists.get(i).get(1).substring(11,13);
                if (maps.get(lists.get(i).get(0)).containsKey(temp)) {
                    Integer s = maps.get(lists.get(i).get(0)).get(temp);
                    maps.get(lists.get(i).get(0)).put(temp,s+1);
                }else{
                    maps.get(lists.get(i).get(0)).put(temp,1);
                }
            }else{
                Map<String, Integer> list = new HashMap<>();
                String temp = lists.get(i).get(1).substring(11,13);
                list.put(temp,1);
                maps.put(lists.get(i).get(0),list);
            }
        }

        Iterator iter = maps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            Log.i("maps","key:"+key+" val:"+val);
        }

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        //.setAdapter(adapter)
                        .setContentHolder(new ViewHolder(R.layout.dialog_layout))
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.dialog_header_layout)
                        // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                TextView text = view.findViewById(R.id.text3);
                String name = text.getText().toString();
                Log.i("maps","获取到名字"+name);
                dialog.show();
                Map<String, Integer> demo = new HashMap<>();
                for(Map.Entry<String, Map<String, Integer>> entry : maps.entrySet()){
                    String mapKey = entry.getKey();
                    if (mapKey.equals(name)){
                        demo = entry.getValue();
                        break;
                    }
                }
                for(Map.Entry<String, Integer> entry : demo.entrySet()){
                    String mapKey = entry.getKey();
                    Integer val = entry.getValue();
                    Log.i("maps", mapKey + "++"+val);
                }
                GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, demo.containsKey("00")?demo.get("00"):0),
                        new DataPoint(1, demo.containsKey("01")?demo.get("01"):0),
                        new DataPoint(2, demo.containsKey("02")?demo.get("02"):0),
                        new DataPoint(3, demo.containsKey("03")?demo.get("03"):0),
                        new DataPoint(4, demo.containsKey("04")?demo.get("04"):0),
                        new DataPoint(5, demo.containsKey("05")?demo.get("05"):0),
                        new DataPoint(6, demo.containsKey("06")?demo.get("06"):0),
                        new DataPoint(7, demo.containsKey("07")?demo.get("07"):0),
                        new DataPoint(8, demo.containsKey("08")?demo.get("08"):0),
                        new DataPoint(9, demo.containsKey("09")?demo.get("09"):0),
                        new DataPoint(10, demo.containsKey("10")?demo.get("10"):0),
                        new DataPoint(11, demo.containsKey("11")?demo.get("11"):0),
                        new DataPoint(12, demo.containsKey("12")?demo.get("12"):0),
                        new DataPoint(13, demo.containsKey("13")?demo.get("13"):0),
                        new DataPoint(14, demo.containsKey("14")?demo.get("14"):0),
                        new DataPoint(15, demo.containsKey("15")?demo.get("15"):0),
                        new DataPoint(16, demo.containsKey("16")?demo.get("16"):0),
                        new DataPoint(17, demo.containsKey("17")?demo.get("17"):0),
                        new DataPoint(18, demo.containsKey("18")?demo.get("18"):0),
                        new DataPoint(19, demo.containsKey("19")?demo.get("19"):0),
                        new DataPoint(20, demo.containsKey("20")?demo.get("20"):0),
                        new DataPoint(21, demo.containsKey("21")?demo.get("21"):0),
                        new DataPoint(22, demo.containsKey("22")?demo.get("22"):0),
                        new DataPoint(23, demo.containsKey("23")?demo.get("23"):0),
                        new DataPoint(24, demo.containsKey("24")?demo.get("24"):0),
                });
                graph.addSeries(series);
            }
        });
        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "phone_data.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(CREATE_PHONE_DATA);


        // 起始时间 设置为2020/10/27 00:00:00
        df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

         start_date_string = "2020/10/29 00:00:00";
         start_date = null; // 起始时间

         end_date = new Date(); // 结束时间就是现在
         end_date_string = df.format(end_date);

        try {
            end_date = df.parse(end_date_string);
            start_date = df.parse(start_date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            // 把所有的应用（此时间段启动过的） 存入到数据库中
            getUsageList(getActivity(), start_date.getTime(), end_date.getTime());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("test", "获取数据结束");
    }
    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getUsageList(Context context, long startTime, long endTime) throws PackageManager.NameNotFoundException {
        /**
         * 获取一段时间内的应用使用情况
         */

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists data");
        db.execSQL(CREATE_PHONE_DATA);

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime, endTime);
        for (Map.Entry<String, UsageStats> entry : map.entrySet()) {
            UsageStats stats = entry.getValue();
            PackageManager pm = getActivity().getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(stats.getPackageName(), PackageManager.GET_META_DATA);

            ContentValues values = new ContentValues();
            // 获取应用名称
            String name = (String) pm.getApplicationLabel(applicationInfo);
            String packageName = applicationInfo.packageName;
            Log.i("test", "name" + name);

            // 获取应用图标
            Drawable icon = applicationInfo.loadIcon(getActivity().getPackageManager());
            Bitmap img = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && icon instanceof AdaptiveIconDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                icon.draw(canvas);
                img = bitmap;
            } else {
                BitmapDrawable d = (BitmapDrawable) icon;
                img = d.getBitmap();
            }

            String fn = name + ".png";
            String path = getActivity().getFilesDir() + File.separator + fn;
            String image = path;
            try {
                OutputStream os = new FileOutputStream(path);
                img.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
            } catch (Exception e) {
                Log.e("TAG", "", e);
            }

            String last = dateFormat.format(new Date(stats.getLastTimeUsed()));
            Log.i("test", "name:" + name + "path:" + path);
            if (stats.getTotalTimeInForeground() >= 0) {
                values.put("name", name);
                values.put("image", image);
                values.put("package",packageName);
                values.put("last_time", last);
                db.insert("data", null, values);
            }
        }

    }


    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<ArrayList<String>> getEventList(Context context, long startTime, long endTime) {
        /**
         * 获取时间序列
         */
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        UsageEvents events = mUsmManager.queryEvents(startTime, endTime);
        while (events.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            events.getNextEvent(e);
            String stime = dateFormat.format(new Date(e.getTimeStamp()));
            if (e != null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(e.getPackageName());
                list.add(stime);
                lists.add(list);
            }
        }

        return lists;
    }


    public ArrayList<Map<String, Object>> getData() {
        // 从数据库读取数据到listView
        dbHelper = new MyDatabaseHelper(mView.getContext(), "phone_data.db", null, 1);
        Cursor cursor = dbHelper.getReadableDatabase().query("data", null, null, null, null, null, "-last_time");
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String packageName = cursor.getString(cursor.getColumnIndex("package"));
                Bitmap bitmap = BitmapFactory.decodeFile(image);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("image", drawable);
                map.put("last_time", last_time);
                map.put("package",packageName);
                lists.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lists;
    }
}