package com.demo.appmonitor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ResearchItem;

import java.util.List;

public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {
    private List<ResearchItem> mItemlist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View researchItemView;
        ImageView appImage;
        TextView appName;
        TextView packageName;
        TextView lastTime;
        public ViewHolder(View view){
            super(view);
            researchItemView = view;
            appImage = view.findViewById(R.id.research_appimage);
            appName = view.findViewById(R.id.research_appname);
            packageName = view.findViewById(R.id.research_packagename);
            lastTime = view.findViewById(R.id.research_last);
        }
    }
    public ResearchAdapter(List<ResearchItem> appList){
        mItemlist = appList;
    }

    @NonNull
    @Override
    public ResearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.research_list_item,parent,false);
        final ResearchAdapter.ViewHolder holder = new ResearchAdapter.ViewHolder(view);
        holder.researchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //显示统计图
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = mItemlist.get(position);
        holder.appName.setText(item.getAppName());
        holder.packageName.setText(item.getPackageName());
        holder.lastTime.setText(item.getLastTime());
        //加载app图片
    }

    @Override
    public int getItemCount() {
        return mItemlist.size();
    }

}
