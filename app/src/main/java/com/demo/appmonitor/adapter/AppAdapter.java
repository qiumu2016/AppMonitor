package com.demo.appmonitor.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.R;
import com.demo.appmonitor.model.AppItem;
import com.demo.appmonitor.model.ResearchItem;


import java.util.List;
import java.util.Map;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private List<AppItem> mItemlist;
    private MyDatabaseHelper dbHelper;
    private Context context;
    private Activity activity;
    private Map<String, Map<String, Integer>> maps;

    public void setContext(Context context) {
        this.context = context;
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View appItemView;
        ImageView image;
        TextView appName;
        TextView packageName;
        TextView type;
        public ViewHolder(View view){
            super(view);
            appItemView = view;
            image = view.findViewById(R.id.app_image);
            appName = view.findViewById(R.id.app_name);
            packageName = view.findViewById(R.id.app_packagename);
            type = view.findViewById(R.id.app_type);
        }
    }
    public AppAdapter(List<AppItem> appList){
        mItemlist = appList;
    }
    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applist_item,parent,false);
        final AppAdapter.ViewHolder holder = new AppAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.ViewHolder holder, int position) {
        AppItem item = mItemlist.get(position);
        holder.appName.setText(item.getAppName());
        holder.packageName.setText(item.getPackageName());
        holder.type.setText(item.getType());
        //加载app图片
        holder.image.setImageDrawable(Drawable.createFromPath(item.getImage()));
    }

    @Override
    public int getItemCount() {
        return mItemlist.size();
    }
}
