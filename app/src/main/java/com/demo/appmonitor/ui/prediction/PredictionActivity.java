package com.demo.appmonitor.ui.prediction;

import android.annotation.SuppressLint;
import android.content.Context;
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

import android.os.ParcelUuid;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;

import android.widget.Toast;
import com.demo.appmonitor.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
    private int []data_1 = new int[3]; // 返回的应用序列号
    private String []package_1 = new String[]{"","",""}; // 返回的包名
    public ProfileActivityAdapter adapter;
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
                String a = adapter.getChooseA();
                String b = adapter.getChooseB();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()

                        .url("http://192.168.189.1:8000/demo/?app1=1&app2=0")
                        .get()
                        .addHeader(a, b)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String jsonData = response.body().string();
                        JSONObject Jobject = null;
                        try {
                            Jobject = new JSONObject(jsonData);
                            JSONArray data = Jobject.getJSONArray("data");
                            JSONArray package_name = Jobject.getJSONArray("package_name");
                            data_1[0] = (int) data.get(0);
                            data_1[1] = (int) data.get(1);
                            data_1[2] = (int) data.get(2);
                            package_1[0] = (String) package_name.get(0);
                            package_1[1] = (String) package_name.get(1);
                            package_1[2] = (String) package_name.get(2);
                            Log.i("dong", data_1[0] + package_1[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        

//                        onsuccess(call,response);
                    }
                });

            }
        });
    }
    // 接受信息成功用于显示
    @SuppressLint("WrongConstant")
    private void onsuccess(@NotNull Call call, @NotNull Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
                public void run() {
                Context context = PredictionActivity.this;
                // 在这里加入推荐应用的图像即可
                fab1.setImageResource(R.drawable.bilibili);
                fab1.setVisibility(0);
                fab2.setImageResource(R.drawable.tengxun);
                fab2.setVisibility(0);
                fab3.setImageResource(R.drawable.taobak);
                fab3.setVisibility(0);
            }
        });

    }
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview_activity);
        adapter = new ProfileActivityAdapter(activityList);
        recyclerView.setAdapter(adapter);
        adapter.usingByPrediction();
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