package com.demo.appmonitor.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ProfileActivityItem;
import com.demo.appmonitor.ui.home.ProfileActivityAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private List<ProfileActivityItem> activityList = new ArrayList<ProfileActivityItem>();;
    private View mContentView;
    private SwipeRefreshLayout swipe_refresh_layout;
    private CircleImageView logo;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        email = (TextView)mContentView.findViewById(R.id.fra_pro_email);
        email.setText("3527995642@qq.com");
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:3527995642@qq.com"));
                startActivity(data);

            }
        });
        initData();
        initRecyclerView();
        return mContentView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id.recylerview_activity);
        ProfileActivityAdapter adapter = new ProfileActivityAdapter(activityList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }
    //用于初始化假数据
    private void initData() {

            ProfileActivityItem apple = new ProfileActivityItem(("quimu2016"), R.drawable.yue, "添加时间选择器样例,完成异步加载，删除不必要代码");
            activityList.add(apple);
            ProfileActivityItem banana = new ProfileActivityItem(("E83737664"), R.drawable.geng, "加入了邮箱跳转和浏览器中打开");
            activityList.add(banana);
            ProfileActivityItem orange = new ProfileActivityItem(("LinearLaw"), R.drawable.yang, "图片改名，新增view pager");
            activityList.add(orange);
            ProfileActivityItem watermelon = new ProfileActivityItem(("dongw0213"), R.drawable.dong26163883, "调整应用分析样式");
            activityList.add(watermelon);
            ProfileActivityItem pear = new ProfileActivityItem( ("dongw0213"), R.drawable.dong26163883, "更新fragment，启动应用授权验证，应用分析界面调整，sql按时间倒序");
            activityList.add(pear);
            ProfileActivityItem grape = new ProfileActivityItem( ("LinearLaw"), R.drawable.yang, "应用序列展示当前应用");
            activityList.add(grape);
            ProfileActivityItem pineapple = new ProfileActivityItem( ("E83737664"), R.drawable.geng, "增加了主页的界面");
            activityList.add(pineapple);
            ProfileActivityItem strawberry = new ProfileActivityItem( ("quimu2016"), R.drawable.yue, "删除AppInfo，改用AppItem");
            activityList.add(strawberry);
            ProfileActivityItem cherry = new ProfileActivityItem( ("quimu2016"), R.drawable.yue, "新增应用序列获取所有应用列表 - not completed");
            activityList.add(cherry);
            ProfileActivityItem mango = new ProfileActivityItem( ("dongw0213"), R.drawable.dong26163883, "修改ui");
            activityList.add(mango);

    }



}