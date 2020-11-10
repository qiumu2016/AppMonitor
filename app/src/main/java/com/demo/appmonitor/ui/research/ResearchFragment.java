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
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import com.demo.appmonitor.viewmodel.ResearchViewModel;
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
import java.util.Map;

import static android.content.Context.USAGE_STATS_SERVICE;

public class ResearchFragment extends Fragment {

    private ResearchViewModel researchViewModel;
    private RecyclerView recyclerView;
    private View mContentView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_research, container, false);
        initRecyclerView();
        progressBar = mContentView.findViewById(R.id.reseach_progress_bar);
        recyclerView.setVisibility(View.GONE);
        try {
            researchViewModel.setContext(this.getContext());
            researchViewModel.flashList();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        researchViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ResearchItem>>() {
            @Override
            public void onChanged(ArrayList<ResearchItem> researchItems) {
                ResearchAdapter adapter = new ResearchAdapter(researchItems);
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
        researchViewModel = new ViewModelProvider(this).get(ResearchViewModel.class);
        researchViewModel.setContext(this.getActivity());
    }

    private void initRecyclerView() {
        recyclerView = mContentView.findViewById(R.id.reseach_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}