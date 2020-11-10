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
import com.demo.appmonitor.domain.AppInfo;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_applist, container, false);
        initRecyclerView();
        progressBar = mContentView.findViewById(R.id.applist_progress_bar);
        recyclerView.setVisibility(View.GONE);
//        applistViewModel.flashList();
        applistViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<AppItem>>() {
            @Override
            public void onChanged(ArrayList<AppItem> appItems) {
                AppAdapter adapter = new AppAdapter(appItems);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //appListEntry();
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
    /**
     * @desc 获取app list，渲染到fragment
     */
//    public void appListEntry(){
//
//        Context ctx = getActivity();
//        if(ctx != null){
//            // 1、获取packageManager
//            pm = ctx.getPackageManager();
//
//            // 2、获取应用列表，可以传入不同的type获取 系统程序 or 第三方应用
//            ArrayList<HashMap<String, Object>> list = getAppList(FILTER_ALL_APP);
//
//            // 3、将应用列表渲染到界面上
//            renderList(list);
//        }
//    }

//    public static final int FILTER_ALL_APP = 0; // 所有应用程序
//    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
//    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
//    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
//    private PackageManager pm;
    /**
     * 获取应用列表，根据传入的type的不同，指定过滤不同类型的应用
     */
//    private ArrayList<HashMap<String, Object>> getAppList(int type) {
//
//        // 查询已经安装的应用程序
//        List<PackageInfo> applicationInfos = pm.getInstalledPackages(0);
//
//        ArrayList<HashMap<String, Object>> displayList = new ArrayList<HashMap<String, Object>>();;
//        switch (type) {
//            case FILTER_ALL_APP:// 所有应用
//                for (PackageInfo appInfo : applicationInfos) {
//                    HashMap<String, Object> temp = getAppInfo(appInfo);
//
//                    displayList.add(temp);
//                }
//                break;
//            case FILTER_SYSTEM_APP:// 系统应用
//                for (PackageInfo appInfo : applicationInfos) {
//                    if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                        HashMap<String, Object> temp = getAppInfo(appInfo);
//                        displayList.add(temp);
//                    }
//                }
//            case FILTER_THIRD_APP:// 第三方应用
//
//                for (PackageInfo appInfo : applicationInfos) {
//                    // 非系统应用
//                    if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
//                        HashMap<String, Object> temp = getAppInfo(appInfo);
//                        displayList.add(temp);
//                    }
//                    // 系统应用，但更新后变成不是系统应用了
//                    else if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
//                        HashMap<String, Object> temp = getAppInfo(appInfo);
//                        displayList.add(temp);
//                    }
//                }
//            case FILTER_SDCARD_APP:// SDCard应用
//                for (PackageInfo appInfo : applicationInfos) {
//                    if (appInfo.applicationInfo.flags == ApplicationInfo.FLAG_SYSTEM) {
//                        HashMap<String, Object> temp = getAppInfo(appInfo);
//                        displayList.add(temp);
//                    }
//                }
//            default:
//                break;
//        }
//
//        return displayList;
//    }
    /**
     * 获取应用信息
     */
//    private HashMap<String, Object> getAppInfo(PackageInfo pkgInfo) {
////        AppInfo appInfo = new AppInfo();
////        appInfo.setAppIcon(applicationInfo.loadIcon(pm)); // 应用图标
////        appInfo.setAppName(applicationInfo.loadLabel(pm).toString()); // 应用名
////        appInfo.setPackageName(applicationInfo.packageName); // 包名
////        appInfo.setFlags(applicationInfo.flags);
////        Log.i("【APP INFO】","应用：\n" + appInfo.getAppName()
////                + "\n包名：" + appInfo.getPackageName()
////                + "\n应用类型："+ appInfo.getAppTypeCN()
////        );
//        ApplicationInfo applicationInfo = pkgInfo.applicationInfo;
//        AppInfo temp = new AppInfo();
//        temp.setFlags(applicationInfo.flags);
//
//        HashMap<String, Object> appInfo = new HashMap<String, Object>();
//        appInfo.put("appIcon",applicationInfo.loadIcon(pm)); // 应用图标
//        appInfo.put("appName",applicationInfo.loadLabel(pm).toString()); // 应用名
//        appInfo.put("packageName",applicationInfo.packageName); // 包名
//        appInfo.put("type",temp.getAppTypeCN());
////        appInfo.put("install_time",pkgInfo.firstInstallTime);
////        appInfo.put("versionCode",pkgInfo.versionCode);
////        appInfo.put("versionName",pkgInfo.versionName);
//
//
//        return appInfo;
//    }

    /**
     *  渲染列表
     */
//    private void renderList(ArrayList<HashMap<String, Object>> list){
//        ListView listView = getActivity().findViewById(R.id.fragment_list);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(
//                getActivity(),
//                list,
//                R.layout.applist_item,
//                new String[]{"appIcon","appName","packageName","type"},
//                new int[]{
//                        R.id.app_name,
//                        R.id.research_last,
//                        R.id.research_packagename,
//                        R.id.text4
//                }
//        );
//        // 这个不加 图片显示不出来
//        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation) {
//                if (view instanceof ImageView && data instanceof Drawable) {
//                    ImageView iv = (ImageView) view;
//                    iv.setImageDrawable((Drawable) data);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        listView.setAdapter(simpleAdapter);
//    }
}