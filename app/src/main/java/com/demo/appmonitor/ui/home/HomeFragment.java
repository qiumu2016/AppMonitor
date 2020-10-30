package com.demo.appmonitor.ui.home;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.appmonitor.R;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.USAGE_STATS_SERVICE;

public class HomeFragment extends Fragment {
    View mView;

    private TextView textView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.givePermission);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        });
        if (isPermission()) {
            textView.setText("已授权！");
            textView.setClickable(false);
        } else {
            textView.setText("打开授权页面进行授权！");
            textView.setClickable(true);
        }
        mView = root;
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public boolean isPermission() {
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.YEAR, -1);
        Calendar endCal = Calendar.getInstance();
        UsageStatsManager manager = (UsageStatsManager) getActivity().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        if (stats.size() == 0) {
            return false;
        }else {
            return true;
        }
    }
}