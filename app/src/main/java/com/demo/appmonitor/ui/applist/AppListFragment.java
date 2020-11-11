package com.demo.appmonitor.ui.applist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.R;
import com.demo.appmonitor.adapter.AppAdapter;
import com.demo.appmonitor.model.AppItem;
import com.demo.appmonitor.viewmodel.ApplistViewModel;
import com.demo.appmonitor.viewmodel.ResearchViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppListFragment extends Fragment {

    private View mContentView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AppAdapter appAdapter;
    private ApplistViewModel applistViewModel;
    private Thread newThread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_applist, container, false);
        initRecyclerView();
        progressBar = mContentView.findViewById(R.id.applist_progress_bar);
        recyclerView.setVisibility(View.GONE);

        applistViewModel.setContext(this.getContext());
        newThread = new Thread() {
            @Override
            public void run() {
                try {
                    applistViewModel.flashList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        newThread.start();

//        applistViewModel.flashList();
        applistViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<AppItem>>() {
            @Override
            public void onChanged(ArrayList<AppItem> appItems) {
                AppAdapter adapter = new AppAdapter(appItems);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        applistViewModel = new ViewModelProvider(this).get(ApplistViewModel.class);
        applistViewModel.setContext(this.getActivity());
    }
    private void initRecyclerView() {
        recyclerView = mContentView.findViewById(R.id.app_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}