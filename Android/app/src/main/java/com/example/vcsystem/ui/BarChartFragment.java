package com.example.vcsystem.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vcsystem.R;
import com.example.vcsystem.model.getReadModelFHP;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends Fragment {

    private static final String TAG = "TAG";
    private BarChart barChart;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        barChart = root.findViewById(R.id.barChart);

        getReadModelFHP.GetInsideReadModel(new getReadModelFHP.MyCallback() {
            @Override
            public void onCallback(ArrayList<BarEntry> values1, ArrayList<BarEntry> values2, ArrayList<BarEntry> values3, ArrayList<String> date) {
                initBarChart(barChart, values1, date);
                showBarChart(values1, values2, values3);
            }
        });
        //給每個document的版本，此版本須同步更改getReadModelFHP的部分
//        getReadModelFHP.GetReadModel(new getReadModelFHP.MyCallback() {
//            @Override
//            public void onCallback(ArrayList<BarEntry> values1, ArrayList<BarEntry> values2, ArrayList<String> date) {
//                initBarChart(barChart, values1, date);
//                showBarChart(values1, values2);
//            }
//        });

        return root;
    }

    private void initBarChart(BarChart barChart, ArrayList<BarEntry> values1, ArrayList<String>dateValueList) {
        YAxis leftAxis = barChart.getAxisLeft();             //左侧Y轴
        YAxis rightAxis = barChart.getAxisRight();            //右侧Y轴
        XAxis xAxis = barChart.getXAxis();                //X轴
        Legend legend = barChart.getLegend();              //图例

        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);
        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);

        //縮放
        barChart.setScaleEnabled(false);// 禁用縮放及點二下觸摸響應，點擊可顯示高亮線
        barChart.setTouchEnabled(false);// 禁用縮放及點二下觸摸響應，點擊也不顯示高亮線
        barChart.setPinchZoom(true); // true->X、Y軸同時按比例縮放、false:X、Y可單獨縮放

//        leftAxis.setLabelCount(10);//Y軸標籤個數
        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

//        leftAxis.setAxisMinimum(0);//Y軸標籤最小值
//        leftAxis.setAxisMaximum(10);//Y軸標籤最大值

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);


        int i = values1.size();
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(i);
        //将X轴的值显示在中央
        xAxis.setCenterAxisLabels(true);



        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);//不顯示右側Y軸

        //X轴自定义值
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateValueList));

        //左侧Y轴自定义值
//        ArrayList<String> hundredPercent = new ArrayList<>();
//        for (int count = 0; count < 11; count++){
//            hundredPercent.add(count*10 + "%");
//        }
//        leftAxis.setValueFormatter(new IndexAxisValueFormatter(hundredPercent));


        /***折线图例 标签 设置***/
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }

    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
//        barDataSet.setValueTextSize(10f);
//        barDataSet.setValueTextColor(color);
    }

    public void showBarChart(final ArrayList<BarEntry> values1, final ArrayList<BarEntry> values2, final ArrayList<BarEntry> values3) {
        List<IBarDataSet> dataSets = new ArrayList<>();

        BarDataSet barDataSet1 = new BarDataSet(values1, "無頭部前傾(NoFHP)");
        initBarDataSet(barDataSet1, getResources().getColor(R.color.green));

        BarDataSet barDataSet2 = new BarDataSet(values2, "輕度頭部前傾(sFHP)");
        initBarDataSet(barDataSet2, getResources().getColor(R.color.yellow));

        BarDataSet barDataSet3 = new BarDataSet(values3, "中重度頭部前傾(mFHP)");
        initBarDataSet(barDataSet3, getResources().getColor(R.color.red));

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        BarData data = new BarData(dataSets);

        int barAmount = 3; //需要显示柱状图的类别 数量
//设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
        float groupSpace = 0.3f; //柱状图组之间的间距
        float barWidth = (1f - groupSpace) / barAmount;
        float barSpace = 0f;
//设置柱状图宽度
        data.setBarWidth(barWidth);
//(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0f, groupSpace, barSpace);

        barChart.setData(data);//一定要放在最後
        barChart.invalidate();//繪製圖表
    }

}