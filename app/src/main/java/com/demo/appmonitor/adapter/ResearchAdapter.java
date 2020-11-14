package com.demo.appmonitor.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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

import com.demo.appmonitor.MyService;
import com.demo.appmonitor.R;
import com.demo.appmonitor.model.ResearchItem;
import com.demo.appmonitor.MyDatabaseHelper;
import com.demo.appmonitor.viewmodel.ResearchViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.UniqueLegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.orhanobut.dialogplus.DialogPlus;

import java.text.SimpleDateFormat;
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

    public void setMaps(Map<String, Map<String, Integer>> maps) {
        this.maps = maps;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View researchItemView;
        ImageView appImage;
        TextView appName;
        TextView packageName;
        TextView lastTime;

        public ViewHolder(View view) {
            super(view);
            researchItemView = view;
            appImage = view.findViewById(R.id.research_appimage);
            appName = view.findViewById(R.id.research_appname);
            packageName = view.findViewById(R.id.research_packagename);
            lastTime = view.findViewById(R.id.research_last);
        }
    }

    public ResearchAdapter(List<ResearchItem> appList) {
        mItemlist = appList;
    }

    @NonNull
    @Override
    public ResearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.research_list_item, parent, false);
        final ResearchAdapter.ViewHolder holder = new ResearchAdapter.ViewHolder(view);
        holder.researchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                //显示统计图
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_layout))
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.dialog_header_layout)
                        .create();

                TextView text = view.findViewById(R.id.research_packagename);
                String name = text.getText().toString();
                dialog.show();
                Map<String, Integer> demo = new HashMap<>();

                for (Map.Entry<String, Map<String, Integer>> entry : maps.entrySet()) {
                    String mapKey = entry.getKey();
                    if (mapKey.equals(name)) {
                        demo = entry.getValue();
                        break;
                    }
                }

                GraphView graph = (GraphView) activity.findViewById(R.id.graph);

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, demo.containsKey("00") ? demo.get("00") : 0),
                        new DataPoint(1, demo.containsKey("01") ? demo.get("01") : 0),
                        new DataPoint(2, demo.containsKey("02") ? demo.get("02") : 0),
                        new DataPoint(3, demo.containsKey("03") ? demo.get("03") : 0),
                        new DataPoint(4, demo.containsKey("04") ? demo.get("04") : 0),
                        new DataPoint(5, demo.containsKey("05") ? demo.get("05") : 0),
                        new DataPoint(6, demo.containsKey("06") ? demo.get("06") : 0),
                        new DataPoint(7, demo.containsKey("07") ? demo.get("07") : 0),
                        new DataPoint(8, demo.containsKey("08") ? demo.get("08") : 0),
                        new DataPoint(9, demo.containsKey("09") ? demo.get("09") : 0),
                        new DataPoint(10, demo.containsKey("10") ? demo.get("10") : 0),
                        new DataPoint(11, demo.containsKey("11") ? demo.get("11") : 0),
                        new DataPoint(12, demo.containsKey("12") ? demo.get("12") : 0),
                        new DataPoint(13, demo.containsKey("13") ? demo.get("13") : 0),
                        new DataPoint(14, demo.containsKey("14") ? demo.get("14") : 0),
                        new DataPoint(15, demo.containsKey("15") ? demo.get("15") : 0),
                        new DataPoint(16, demo.containsKey("16") ? demo.get("16") : 0),
                        new DataPoint(17, demo.containsKey("17") ? demo.get("17") : 0),
                        new DataPoint(18, demo.containsKey("18") ? demo.get("18") : 0),
                        new DataPoint(19, demo.containsKey("19") ? demo.get("19") : 0),
                        new DataPoint(20, demo.containsKey("20") ? demo.get("20") : 0),
                        new DataPoint(21, demo.containsKey("21") ? demo.get("21") : 0),
                        new DataPoint(22, demo.containsKey("22") ? demo.get("22") : 0),
                        new DataPoint(23, demo.containsKey("23") ? demo.get("23") : 0),
                });
                series.setAnimated(true);
                series.setDrawBackground(true);
                series.setColor(Color.argb(255, 255, 60, 60));
                series.setBackgroundColor(Color.argb(100, 204, 119, 119));
                series.setDrawDataPoints(true);
                graph.addSeries(series);
                graph.getViewport().setMaxX(24);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getGridLabelRenderer().setHighlightZeroLines(false);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Hours");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Times");
                graph.setLegendRenderer(new UniqueLegendRenderer(graph));
                graph.getLegendRenderer().setVisible(true);
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

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


}
