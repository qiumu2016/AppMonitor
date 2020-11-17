package com.demo.appmonitor.ui.prediction;

import android.os.Bundle;


import com.demo.appmonitor.model.ProfileActivityItem;
import com.demo.appmonitor.ui.home.ProfileActivityAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;

import com.demo.appmonitor.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PredictionActivity extends AppCompatActivity {
    private List<ProfileActivityItem> activityList = new ArrayList<ProfileActivityItem>();;
    FloatingActionButton fab1, fab2, fab3; // 用于显示三个返回的预测应用
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        initData();
        initRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        // 点击用于从网络获取信息？
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error","网络未连接");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        onsuccess(call,response);
                    }
                });

            }
        });
    }
    // 接受信息成功用于显示
    private void onsuccess(@NotNull Call call, @NotNull Response response) throws IOException {
        fab1.setVisibility(0);
        fab2.setVisibility(0);
        fab3.setVisibility(0);
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview_activity);
        ProfileActivityAdapter adapter = new ProfileActivityAdapter(activityList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    //应用序列，写死
    private void initData() {

        ProfileActivityItem suning = new ProfileActivityItem(("苏宁"), R.drawable.suningyigou , "开始时间：  结束时间：   持续时间：");
        activityList.add(suning);
        ProfileActivityItem weixin = new ProfileActivityItem(("微信"), R.drawable.weixin, "开始时间：  结束时间：   持续时间：");
        activityList.add(weixin);
        ProfileActivityItem zhifubao = new ProfileActivityItem(("支付宝"), R.drawable.zhifubao, "开始时间：  结束时间：   持续时间：");
        activityList.add(zhifubao);
        ProfileActivityItem jingdong = new ProfileActivityItem(("京东"), R.drawable.jingdong , "开始时间：  结束时间：   持续时间：");
        activityList.add(jingdong);
        ProfileActivityItem taobao = new ProfileActivityItem( ("淘宝"), R.drawable.taobak , "开始时间：  结束时间：   持续时间：");
        activityList.add(taobao);
        ProfileActivityItem bilibili = new ProfileActivityItem( ("bilibili"), R.drawable.bilibili , "开始时间：  结束时间：   持续时间：");
        activityList.add(bilibili);
        ProfileActivityItem qq = new ProfileActivityItem( ("qq"), R.drawable.tengxun, "开始时间：  结束时间：   持续时间：");
        activityList.add(qq);


    }
}