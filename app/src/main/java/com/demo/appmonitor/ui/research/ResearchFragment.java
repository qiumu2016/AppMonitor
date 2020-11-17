package com.demo.appmonitor.ui.research;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Presentation;
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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.appmonitor.MainActivity;
import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.R;
import com.demo.appmonitor.adapter.ResearchAdapter;
import com.demo.appmonitor.model.ResearchItem;
import com.demo.appmonitor.ui.prediction.PredictionActivity;
import com.demo.appmonitor.viewmodel.ResearchViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.USAGE_STATS_SERVICE;

public class ResearchFragment extends Fragment {

    private ResearchViewModel researchViewModel;
    private RecyclerView recyclerView;
    private View mContentView;
    private ProgressBar progressBar;
    private Thread newThread;
    private MyDatabaseHelper dbHelper;
    public Map<String, Map<String, Integer>> maps;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_research, container, false);
        initRecyclerView();
        progressBar = mContentView.findViewById(R.id.reseach_progress_bar);
        recyclerView.setVisibility(View.GONE);
        researchViewModel.setContext(this.getContext());

        newThread = new Thread() {
            @Override
            public void run() {
                try {
                    // 默认是现在的时间 时间戳 -> str
                    Date date = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String date_str = df.format(date);
                    researchViewModel.flashList(date_str);
                } catch (PackageManager.NameNotFoundException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };


        newThread.start();

        researchViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ResearchItem>>() {
            @Override
            public void onChanged(ArrayList<ResearchItem> researchItems) {
                ResearchAdapter adapter = new ResearchAdapter(researchItems);
                adapter.setMaps(maps);
                recyclerView.setAdapter(adapter);
                adapter.setActivity(getActivity());
                adapter.setContext(getContext());
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });
        return mContentView;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        researchViewModel = new ViewModelProvider(this).get(ResearchViewModel.class);
        researchViewModel.setContext(this.getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                maps = getDataList();
            }
        }).start();


    }

    private void initRecyclerView() {
        recyclerView = mContentView.findViewById(R.id.reseach_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                selectTime();
                return true;
            case R.id.action_analyse:
                Intent intent = new Intent(getContext(), PredictionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectTime(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year+ "/" +(monthOfYear+1)+"/"+dayOfMonth+ " 00:00:00";

                        //然后执行以下操作
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        Thread newThread2 = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    researchViewModel.flashList(date);
                                } catch (PackageManager.NameNotFoundException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //异步加载新数据，在ViewModel中设置新的接受时间范围的函数，开新的thread进行加载
                        newThread2.start();
                    }
                },
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.setThemeDark(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.show(getFragmentManager(), "DataPickerDialog");

    }

    public Map<String, Map<String, Integer>> getDataList() {
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        dbHelper = new MyDatabaseHelper(getActivity(), "phone_data.db", null, 1);

        // orderBy加了个DESC，按last_time的倒序查询
        Cursor cursor = dbHelper.getReadableDatabase()
                .query("data_list", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String aPackage = cursor.getString(cursor.getColumnIndex("package"));
                Long stime = cursor.getLong(cursor.getColumnIndex("stime"));
                ArrayList<String> list = new ArrayList<>();
                list.add(aPackage);
                list.add(df.format(stime));
                lists.add(list);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        dbHelper.close();

        // 对list 进行清洗
        for (int i = 0; i < lists.size(); i++) {
            int j = i + 1;
            while (j < lists.size() && (lists.get(i).get(0) == (lists.get(j).get(0)))) {
                lists.remove(j);
                j = i + 1;
            }
        }

        final Map<String, Map<String, Integer>> maps = new HashMap<>();
        for (int i = 0; i < lists.size(); i++) {
            if (maps.containsKey(lists.get(i).get(0))) {
                String temp = lists.get(i).get(1).substring(11, 13);
                if (maps.get(lists.get(i).get(0)).containsKey(temp)) {
                    Integer s = maps.get(lists.get(i).get(0)).get(temp);
                    maps.get(lists.get(i).get(0)).put(temp, s + 1);
                } else {
                    maps.get(lists.get(i).get(0)).put(temp, 1);
                }
            } else {
                Map<String, Integer> list = new HashMap<>();
                String temp = lists.get(i).get(1).substring(11, 13);
                list.put(temp, 1);
                maps.put(lists.get(i).get(0), list);
            }
        }
        return maps;
    }


}