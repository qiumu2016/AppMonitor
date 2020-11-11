package com.demo.appmonitor.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ResearchItem;
import com.demo.appmonitor.MyDatabaseHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {
    private List<ResearchItem> mItemlist;
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
        maps = getDataList();
        holder.researchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //显示统计图
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_layout))
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.dialog_header_layout)
                        // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                TextView text = view.findViewById(R.id.research_packagename);
                String name = text.getText().toString();
                Log.i("maps","获取到名字"+name);
                dialog.show();
                Map<String, Integer> demo = new HashMap<>();
                for(Map.Entry<String, Map<String, Integer>> entry : maps.entrySet()){
                    String mapKey = entry.getKey();
                    if (mapKey.equals(name)){
                        demo = entry.getValue();
                        break;
                    }
                }
                for(Map.Entry<String, Integer> entry : demo.entrySet()){
                    String mapKey = entry.getKey();
                    Integer val = entry.getValue();
                    Log.i("maps", mapKey + "++"+val);
                }
                GraphView graph = (GraphView) activity.findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, demo.containsKey("00")?demo.get("00"):0),
                        new DataPoint(1, demo.containsKey("01")?demo.get("01"):0),
                        new DataPoint(2, demo.containsKey("02")?demo.get("02"):0),
                        new DataPoint(3, demo.containsKey("03")?demo.get("03"):0),
                        new DataPoint(4, demo.containsKey("04")?demo.get("04"):0),
                        new DataPoint(5, demo.containsKey("05")?demo.get("05"):0),
                        new DataPoint(6, demo.containsKey("06")?demo.get("06"):0),
                        new DataPoint(7, demo.containsKey("07")?demo.get("07"):0),
                        new DataPoint(8, demo.containsKey("08")?demo.get("08"):0),
                        new DataPoint(9, demo.containsKey("09")?demo.get("09"):0),
                        new DataPoint(10, demo.containsKey("10")?demo.get("10"):0),
                        new DataPoint(11, demo.containsKey("11")?demo.get("11"):0),
                        new DataPoint(12, demo.containsKey("12")?demo.get("12"):0),
                        new DataPoint(13, demo.containsKey("13")?demo.get("13"):0),
                        new DataPoint(14, demo.containsKey("14")?demo.get("14"):0),
                        new DataPoint(15, demo.containsKey("15")?demo.get("15"):0),
                        new DataPoint(16, demo.containsKey("16")?demo.get("16"):0),
                        new DataPoint(17, demo.containsKey("17")?demo.get("17"):0),
                        new DataPoint(18, demo.containsKey("18")?demo.get("18"):0),
                        new DataPoint(19, demo.containsKey("19")?demo.get("19"):0),
                        new DataPoint(20, demo.containsKey("20")?demo.get("20"):0),
//                        new DataPoint(21, demo.containsKey("21")?demo.get("21"):0),
//                        new DataPoint(22, demo.containsKey("22")?demo.get("22"):0),
//                        new DataPoint(23, demo.containsKey("23")?demo.get("23"):0),
//                        new DataPoint(24, demo.containsKey("24")?demo.get("24"):0),
                });
                graph.addSeries(series);

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
        holder.appImage.setImageDrawable(Drawable.createFromPath(item.getImageId()));
    }

    @Override
    public int getItemCount() {
        return mItemlist.size();
    }

    public Map<String, Map<String, Integer>> getDataList() {
//        Map<String, Map<String, Integer>> lists = new HashMap<>();
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        dbHelper = new MyDatabaseHelper(context, "phone_data.db", null, 1);

        // orderBy加了个DESC，按last_time的倒序查询
        Cursor cursor = dbHelper.getReadableDatabase()
                .query("data_list", null, null, null, null, null, "stime DESC");
        if (cursor.moveToFirst()) {
            do {
                String aPackage = cursor.getString(cursor.getColumnIndex("package"));
                String stime = cursor.getString(cursor.getColumnIndex("stime"));
                ArrayList<String> list = new ArrayList<>();
                list.add(aPackage);
                list.add(stime);
                lists.add(list);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 对list 进行清洗
        for (int i = 0; i < lists.size(); i++) {
            int j = i + 1;
            while (j < lists.size() && (lists.get(i).get(0) == (lists.get(j).get(0)))) {
                lists.remove(j);
                j = i + 1;
            }
        }

        final Map<String, Map<String, Integer> > maps = new HashMap<>();
        for (int i = 0; i < lists.size(); i++) {
            if (maps.containsKey(lists.get(i).get(0))){
                String temp = lists.get(i).get(1).substring(11,13);
                if (maps.get(lists.get(i).get(0)).containsKey(temp)) {
                    Integer s = maps.get(lists.get(i).get(0)).get(temp);
                    maps.get(lists.get(i).get(0)).put(temp,s+1);
                }else{
                    maps.get(lists.get(i).get(0)).put(temp,1);
                }
            }else{
                Map<String, Integer> list = new HashMap<>();
                String temp = lists.get(i).get(1).substring(11,13);
                list.put(temp,1);
                maps.put(lists.get(i).get(0),list);
            }
        }
        return maps;
    }

}
