package com.demo.appmonitor.viewmodel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.demo.appmonitor.MainActivity;
import com.demo.appmonitor.R;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.appmonitor.model.AppItem;
import com.demo.appmonitor.model.ResearchItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplistViewModel extends ViewModel {
    private MutableLiveData<ArrayList<AppItem>> data;
    private Context context;
    List<AppItem> appList = new ArrayList<>();
    public ApplistViewModel() {
        data = new MutableLiveData<>();
    }
    public LiveData<ArrayList<AppItem>> getList() {
        return data;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void flashList(){
        appList.clear();

        // 获取App list
        appList = getAppListEntry();

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 向appList中添加数据
                data.setValue((ArrayList<AppItem>) appList);
            }
        });
    }


    /**
     * @desc 获取应用列表 入口
     */
    public List<AppItem> getAppListEntry(){

        Context ctx = this.context;
        if(ctx != null){
            // 1、获取packageManager
            pm = ctx.getPackageManager();

            // 2、获取应用列表，可以传入不同的type获取 系统程序 or 第三方应用
            ArrayList<AppItem> list = getAppList(FILTER_ALL_APP);
            return list;
        }

        return appList;
    }

    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
    private PackageManager pm;
    /**
     * 获取应用列表，根据传入的type的不同，指定过滤不同类型的应用
     */
    private ArrayList<AppItem> getAppList(int type) {

        // 查询已经安装的应用程序
        List<PackageInfo> applicationInfos = pm.getInstalledPackages(0);

        ArrayList<AppItem> displayList = new ArrayList<AppItem>();;
        switch (type) {
            case FILTER_ALL_APP:// 所有应用
                for (PackageInfo appInfo : applicationInfos) {
                    AppItem temp = getAppInfo(appInfo);

                    displayList.add(temp);
                }
                break;
            case FILTER_SYSTEM_APP:// 系统应用
                for (PackageInfo appInfo : applicationInfos) {
                    if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        AppItem temp = getAppInfo(appInfo);
                        displayList.add(temp);
                    }
                }
            case FILTER_THIRD_APP:// 第三方应用

                for (PackageInfo appInfo : applicationInfos) {
                    // 非系统应用
                    if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        AppItem temp = getAppInfo(appInfo);
                        displayList.add(temp);
                    }
                    // 系统应用，但更新后变成不是系统应用了
                    else if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        AppItem temp = getAppInfo(appInfo);
                        displayList.add(temp);
                    }
                }
            case FILTER_SDCARD_APP:// SDCard应用
                for (PackageInfo appInfo : applicationInfos) {
                    if (appInfo.applicationInfo.flags == ApplicationInfo.FLAG_SYSTEM) {
                        AppItem temp = getAppInfo(appInfo);
                        displayList.add(temp);
                    }
                }
            default:
                break;
        }

        return displayList;
    }
    /**
     * 获取应用信息
     */
    private AppItem getAppInfo(PackageInfo pkgInfo) {
        ApplicationInfo applicationInfo = pkgInfo.applicationInfo;

        AppItem appInfo = new AppItem();

        String name = (String) pm.getApplicationLabel(applicationInfo);
        String image = context.getFilesDir() + File.separator + name + ".png";
        appInfo.setImage(image); // 应用图标

        // /data/user/0/com.demo.appmonitor/files/YouTube.png
        Log.i("image",image);

        appInfo.setAppName(applicationInfo.loadLabel(pm).toString()); // 应用名
        appInfo.setPackageName(applicationInfo.packageName); // 包名
        appInfo.setType(AppItem.getAppTypeCN(applicationInfo.flags));

        return appInfo;
    }

}
