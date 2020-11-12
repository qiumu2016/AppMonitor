package com.demo.appmonitor.ui.home;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ProfileActivityItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
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
    public ProfileActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_activity_item, parent, false);
        final ProfileActivityAdapter.ViewHolder holder = new ProfileActivityAdapter.ViewHolder(view);
        holder.activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toasty.info(v.getContext(),"您点击了该提交记录" , Toast.LENGTH_SHORT,true).show();
            }
        });
        holder.activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String rep = mActivityList.get(position).getName();
                Toasty.info(v.getContext(),"您点击了:" + rep, Toast.LENGTH_SHORT,true).show();
            }
        });
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
