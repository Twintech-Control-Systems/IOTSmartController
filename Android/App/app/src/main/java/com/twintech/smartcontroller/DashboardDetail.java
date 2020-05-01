package com.twintech.smartcontroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sccomponents.gauges.gr004.GR004;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardDetail extends AppCompatActivity {
    GR004 gauge;
    TextView Name,Value,Unit;
    Button Live,History;
    ToggleButton toggleGraphButton;
    GraphView graph;
    private Timer myTimer;
    DBHelper mydb;
    private int channelScanPeriod = 1000;
    private long seconds = 0;
    int channelNo = 0;
    int liveValue = 0;
    String liveDate = "";
    String liveTime = "";
    LineGraphSeries<DataPoint> series;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mydb = new DBHelper(this);
        Name = findViewById(R.id.detailChannelName);
        Value = findViewById(R.id.detailChannelValue);
        Unit = findViewById(R.id.detailChannelUnit);
        graph =  findViewById(R.id.graph);
        gauge = findViewById(R.id.gauge);
        gauge.setEnableTouch(true);
        gauge.setValue(0);
        gauge.setMinValue(0);
        gauge.setMaxValue(100);
        toggleGraphButton= findViewById(R.id.toggleGraphButton);

        toggleGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                series.resetData(new DataPoint[] {
                        new DataPoint(0, 0)});
                if(toggleGraphButton.isChecked()){
                    long count = 0;
                    new Thread(new Runnable() {
                        public void run() {
                            long xAxisCount = 0;
                            //progressBar.();
                            Cursor rs = mydb.getData(channelNo,"ChannelValues");
                            rs.moveToFirst();
                            while(rs.isAfterLast() == false){
                                series.appendData(new DataPoint(xAxisCount, Double.valueOf(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_VALUE))).intValue()),
                                        true,1000);
                                rs.moveToNext();
                                xAxisCount++;
                            }
                        }
                    }).start();


                }
            }
        });
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            Name.setText(mBundle.getString("Name"));
            Value.setText(mBundle.getString("Value"));
            Unit.setText(mBundle.getString("Unit"));
            channelNo = Integer.valueOf(mBundle.getString("Number"));
            Log.d("MyTag",String.valueOf(channelNo));
        }
        graph.getViewport().setScalableY(true);
        graph.setTitle(Name.getText().toString());
        // activate vertical scrolling
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                getChannelData();
            }

        }, 0, channelScanPeriod);

    }

    void getChannelData(){
        getDataFromDB();
        if(!toggleGraphButton.isChecked()){
            series.appendData(new DataPoint(seconds, liveValue), true, 1000);
            seconds++;
        }
        Value.setText(String.valueOf(liveValue));
        gauge.setValue(liveValue);
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // get data from database
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void getDataFromDB(){
            Cursor rs = mydb.getCurrentChannelValue(channelNo);
            rs.moveToFirst();

            liveValue = Double.valueOf(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_VALUE))).intValue();
            liveDate= rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_DATE));
            liveTime = rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_TIME));

    }
}
