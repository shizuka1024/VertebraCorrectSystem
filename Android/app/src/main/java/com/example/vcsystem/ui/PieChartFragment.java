package com.example.vcsystem.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.vcsystem.R;
import com.example.vcsystem.model.getReadModelforPie;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class PieChartFragment extends Fragment {

    private static final String TAG = "TAG";
    private PieChart pieChart;
    private Spinner pieChartspinner;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String userID;
    ArrayList<Contact> pieSpinnerArray = new ArrayList<>();

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
        View root = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        pieChart = root.findViewById(R.id.pieChart);
        pieChartspinner = root.findViewById(R.id.pieChartspinner);
        initSpinnerUI();
        pieChartspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "Chart Ready"+adapterView.getItemAtPosition(position).toString());
                String msg = adapterView.getItemAtPosition(position).toString();
                PieChartUI();
                getReadModelforPie.GetReadModel(new String(msg), new getReadModelforPie.MyCallback() {
                    @Override
                    public void onCallback(ArrayList<PieEntry> values) {
                        setPieData(values);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return root;
    }

    private void initSpinnerUI() {
        firebaseFirestore.collection("users")
                .document(userID)
                .collection("pieGraph")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            int i = 1;
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                arrayList.add(querySnapshot.getString("dateSent3"));
                                i++;
                            }
                            Log.d(TAG, "spinner"+pieSpinnerArray);
                            pieSpinnerArray = getSingle(arrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,pieSpinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pieChartspinner.setAdapter(adapter);
                    }
                });
    }

    private void PieChartUI() {
        Log.d(TAG, "PieChartUI");

// 设置 pieChart 图表基本属性
        pieChart.setUsePercentValues(false);            //使用百分比显示
        pieChart.getDescription().setEnabled(false);    //设置pieChart图表的描述
//        pieChart.setBackgroundColor(Color.YELLOW);      //设置pieChart图表背景色
//        pieChart.setExtraOffsets(5, 10, 60, 10);        //设置pieChart图表上下左右的偏移，类似于外边距
        pieChart.setRotationAngle(0);                   //设置pieChart图表起始角度
        pieChart.setRotationEnabled(true);              //设置pieChart图表是否可以手动旋转
        pieChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
//        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果

// 设置 pieChart 图表Item文本属性
        pieChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        pieChart.setEntryLabelColor(Color.BLACK);       //设置pieChart图表文本字体颜色
//        pieChart.setEntryLabelTypeface(mTfRegular);     //设置pieChart图表文本字体样式
        pieChart.setEntryLabelTextSize(10f);            //设置pieChart图表文本字体大小

// 设置 pieChart 内部圆环属性
        pieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        pieChart.setHoleRadius(28f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(31f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        pieChart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        pieChart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        pieChart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色
        pieChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
//        pieChart.setCenterTextTypeface(mTfLight);       //设置PieChart内部圆文字的字体样式
        pieChart.setCenterText("1小時內該磅數總承受時數");                 //设置PieChart内部圆文字的内容
        pieChart.setCenterTextSize(10f);                //设置PieChart内部圆文字的大小
        pieChart.setCenterTextColor(Color.BLACK);         //设置PieChart内部圆文字的颜色

        Legend l = pieChart.getLegend();
        l.setEnabled(true);                    //是否启用图列（true：下面属性才有意义）
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.DEFAULT); //设置图例的形状
        l.setFormSize(10);					  //设置图例的大小
        l.setFormToTextSpace(6f);			  //设置每个图例实体中标签和形状之间的间距
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);			  //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setXEntrySpace(10f);				  //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(8f);				  //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);					  //设置比例块Y轴偏移量
        l.setTextSize(14f);					  //设置图例标签文本的大小
        l.setTextColor(Color.parseColor("#ff9933"));//设置图例标签文本的颜色

    }


    private void setPieData(final ArrayList<PieEntry> values) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#4fe7f7"));
        colors.add(Color.parseColor("#62f5b0"));
        colors.add(Color.parseColor("#f7f74f"));
        colors.add(Color.parseColor("#ffc23d"));
        colors.add(Color.parseColor("#ff4036"));
//        饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(values, "1小時內該磅數總承受時數(分鐘)");
        pieDataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(10f);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueTextColor(Color.BLACK);  //设置所有DataSet内数据实体（百分比）的文本颜色
        pieData.setValueTextSize(12f);          //设置所有DataSet内数据实体（百分比）的文本字体大小
        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();                    //将图表重绘以显示设置的属性和数据
    }

    public static ArrayList getSingle(ArrayList list) {
        ArrayList tempList = new ArrayList();          //1,创建新集合
        Iterator it = list.iterator();              //2,根据传入的集合(老集合)获取迭代器
        while (it.hasNext()) {                  //3,遍历老集合
            Object obj = it.next();                //记录住每一个元素
            if (!tempList.contains(obj)) {            //如果新集合中不包含老集合中的元素
                tempList.add(obj);                //将该元素添加
            }
        }
        return tempList;
    }

    private class Contact {
        private String contact_name;
        private String contact_id;

        public Contact() {
        }

        public Contact(String contact_name) {
            this.contact_name = contact_name;
            this.contact_id = contact_name;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getContact_id() {
            return contact_id;
        }

        public void setContact_id(String contact_id) {
            this.contact_id = contact_id;
        }

        /**
         * Pay attention here, you have to override the toString method as the
         * ArrayAdapter will reads the toString of the given object for the name
         *
         * @return contact_name
         */
        @Override
        public String toString() {
            return contact_name;
        }
    }
}