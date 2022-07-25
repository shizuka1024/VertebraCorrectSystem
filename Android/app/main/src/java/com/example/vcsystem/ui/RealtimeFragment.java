package com.example.vcsystem.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.vcsystem.R;
import com.example.vcsystem.model.getReadModelPitch;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RealtimeFragment extends Fragment {

    private static final String TAG = "TAG";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID, mAccount;
    private ScrollView scrollView;
    private TextView mTxtReceive, mRealtimeTxt, mRealtimeCNTxt;
    private int mMaxChars = 100;//Default
    private Handler handler;
    private Runnable runnable;
    //設定日期格式
    SimpleDateFormat sdfonlydate = new SimpleDateFormat("yyyy-MM-dd");

    private LineChart lineChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_realtime, container, false);
        mTxtReceive = (TextView) root.findViewById(R.id.txtReceive);
        scrollView = (ScrollView) root.findViewById(R.id.viewScroll);
        mTxtReceive.setMovementMethod(ScrollingMovementMethod.getInstance());
        mRealtimeTxt = (TextView) root.findViewById(R.id.realtimetxt);
        mRealtimeCNTxt = (TextView) root.findViewById(R.id.realtimecntxt);
        lineChart = root.findViewById(R.id.lineChart);
        addTextToTextView();

        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                getReadModelPitch.GetReadModel(new getReadModelPitch.MyCallback() {
                    @Override
                    public void onCallback(ArrayList<Entry> values1, ArrayList<String> date) {
                        initDataSet(values1);
                        initX(date);//設定X軸樣式
                        initY();//設定Y軸樣式
                        initChartFormat();
                    }
                });
                handler.postDelayed(this, 3000);
            }
        };
        //line
//        getReadModelPitch.GetReadModel(new getReadModelPitch.MyCallback() {
//            @Override
//            public void onCallback(ArrayList<Entry> values1, ArrayList<String> date) {
//                initDataSet(values1);
//                initX(date);//設定X軸樣式
//                initY();//設定Y軸樣式
//                initChartFormat();
//            }
//        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void addTextToTextView() {
        Date date = new Date();
        //進行轉換
        String dateString4 = sdfonlydate.format(date);

        firebaseFirestore.collection("users")
                .document(userID)
                .collection(dateString4)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "Error:" + error.getMessage());
                        } else {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        mTxtReceive.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTxtReceive.append(dc.getDocument().getDouble("pitch").toString() + "\n");
                                                mRealtimeTxt.setText(dc.getDocument().getDouble("pitch").toString());
                                                if(dc.getDocument().getDouble("pitch")<23.51){
                                                    mRealtimeCNTxt.setText("正常");
                                                }else if(dc.getDocument().getDouble("pitch")>35.66){
                                                    mRealtimeCNTxt.setText("嚴重");
                                                }else{
                                                    mRealtimeCNTxt.setText("輕微");
                                                }
                                                int txtLength = mTxtReceive.getEditableText().length();
                                                if (txtLength > mMaxChars) {
                                                    mTxtReceive.getEditableText().delete(0, txtLength - (mMaxChars - 10));
                                                }
                                                scrollView.post(new Runnable() { // Snippet from http://stackoverflow.com/a/4612082/1287554
                                                    @Override
                                                    public void run() {
                                                        scrollView.fullScroll(View.FOCUS_DOWN);
                                                    }
                                                });
                                            }
                                        });
                                        Log.d(TAG, "pitch" + dc.getDocument().getDouble("pitch").toString());
                                        lineChart.notifyDataSetChanged();
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    private void initDataSet(final ArrayList<Entry> values1) {
        final LineDataSet set;
        Log.d(TAG, "al" + values1);

        set = new LineDataSet(values1, "pitch");
        set.setMode(LineDataSet.Mode.LINEAR);//類型為折線
        set.setColor(getResources().getColor(R.color.red));//線的顏色
        set.setLineWidth(1.5f);//線寬
        set.setDrawCircles(true); //不顯示相應座標點的小圓圈(預設顯示)
        set.setDrawValues(true);//不顯示座標點對應Y軸的數字(預設顯示)
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);//折線

        //理解爲多條線的集合
        LineData data = new LineData(set);
        lineChart.setData(data);//一定要放在最後
        lineChart.invalidate();//繪製圖表
    }

    private void initX(ArrayList<String> dateValueList) {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
        xAxis.setTextSize(12);//X軸標籤大小

        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離

        xAxis.setDrawGridLines(false);//不顯示每個座標點對應X軸的線 (預設顯示)

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateValueList));
    }

    private void initY() {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

//        leftAxis.setLabelCount(10);//Y軸標籤個數
//        leftAxis.setAxisMinimum(0);//Y軸標籤最小值
//        leftAxis.setAxisMaximum(10);//Y軸標籤最大值

        /**
         * 格式化軸標籤二種方式：
         * 1、用圖表庫已寫好的類_如上一步驟中X 軸使用
         * 2、自己實現接口_如下Y 軸使用
         * */
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
    }

    class MyYAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###.0");//Y軸數值格式及小數點位數
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value);
        }
    }

    private void initChartFormat() {

        //右下方description label：設置圖表資訊
        Description description = lineChart.getDescription();
        description.setEnabled(true);//不顯示Description Label (預設顯示)

        //左下方Legend：圖例數據資料
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);//不顯示圖例 (預設顯示)

        lineChart.setBackgroundColor(Color.WHITE);//顯示整個圖表背景顏色 (預設灰底)

        //設定沒資料時顯示的內容
        lineChart.setNoDataText("暫時沒有數據");
        lineChart.setNoDataTextColor(Color.BLUE);//文字顏色

        //description label：設置圖表資訊
        description.setText("角度/秒(pitch/sec)");//顯示文字名稱
        description.setTextSize(14);//字體大小
        description.setTextColor(getResources().getColor(R.color.darkh1));//字體顏色
//        description.setPosition(680, 80);//顯示位置座標 (預設右下方)

        //圖表邊框
        lineChart.setDrawBorders(true);//顯示邊框 (預設不顯示)

        //縮放
        lineChart.setScaleEnabled(false);// 禁用縮放及點二下觸摸響應，點擊可顯示高亮線
        lineChart.setTouchEnabled(false);// 禁用縮放及點二下觸摸響應，點擊也不顯示高亮線
        lineChart.setPinchZoom(true); // true->X、Y軸同時按比例縮放、false:X、Y可單獨縮放
    }

}