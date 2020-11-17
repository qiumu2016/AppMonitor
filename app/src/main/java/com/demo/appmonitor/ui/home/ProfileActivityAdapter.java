package com.demo.appmonitor.ui.home;

import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.demo.appmonitor.ui.prediction.PredictionActivity;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
public class ProfileActivityAdapter extends RecyclerView.Adapter<ProfileActivityAdapter.ViewHolder> {
    private List<ProfileActivityItem> mActivityList;
    public String chooseA, chooseB;
    private boolean PredictionFlag = false;
    private int mCnt = 0;
    //初始化ViewHolder,用于缓存已经加载的数据
    static class ViewHolder extends RecyclerView.ViewHolder {
        View activityView;
        CircleImageView activityImage;
        TextView activityName;
        TextView text;
        TextView choose;
        TextView allhave;

        public ViewHolder(View view) {
            super(view);
            activityView = view;
            activityImage = (CircleImageView) view.findViewById(R.id.user_avatar);
            activityName = (TextView) view.findViewById(R.id.user_name);
            text = (TextView) view.findViewById(R.id.profile_starred_name);
            choose = (TextView) view.findViewById(R.id.choosed_item);
            allhave = (TextView) view.findViewById(R.id.action);

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
                int position = holder.getAdapterPosition();
                String rep = mActivityList.get(position).getName();

                if(mCnt == 1) {
                    ProfileActivityItem mItem = mActivityList.get(position);
                    chooseB = rep;
                    holder.choose.setVisibility(0);
                    holder.text.setText("已选中的第二项");
                    Toasty.info(v.getContext(),"您选中了第二项："  + chooseB, Toast.LENGTH_SHORT,true).show();
                    mCnt++;
                }
                if(mCnt == 0) {
                    ProfileActivityItem mItem = mActivityList.get(position);
                    Toasty.info(v.getContext(),"您选中了第一项：" + chooseA, Toast.LENGTH_SHORT,true).show();
                    holder.text.setText("已选中的第一项");
                    holder.choose.setVisibility(0);
                    chooseA = rep;
                    mCnt++;

                }
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

        if(PredictionFlag == true){
            int temp = position + 1;
            holder.text.setText("使用总时间排名，第" +  temp + "名");
            holder.allhave.setText("经常使用的app信息");
        }

    }
    public  void usingByPrediction() {
        PredictionFlag = true;
    }
    public String getChooseA(){
        return  chooseA;
    }
    public String getChooseB(){
        return  chooseB;
    }
    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
