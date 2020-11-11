package com.demo.appmonitor.ui.home;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ProfileActivityItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivityAdapter extends RecyclerView.Adapter<ProfileActivityAdapter.ViewHolder> {
    private List<ProfileActivityItem> mActivityList;
    //初始化ViewHolder,用于缓存已经加载的数据
    static class ViewHolder extends RecyclerView.ViewHolder {
        View activityView;
        CircleImageView activityImage;
        TextView activityName;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            activityView = view;
            activityImage = (CircleImageView) view.findViewById(R.id.user_avatar);
            activityName = (TextView) view.findViewById(R.id.user_name);
            text = (TextView) view.findViewById(R.id.profile_starred_name);


        }
    }
    // 类的的构造函数
    public ProfileActivityAdapter(List<ProfileActivityItem> activityList) {
        mActivityList = activityList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_activity_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfileActivityItem mItem = mActivityList.get(position);
        holder.activityImage.setImageResource(mItem.getImageId());
        holder.activityName.setText(mItem.getName());
        holder.text.setText(mItem.getText());

    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
