package com.twintech.smartcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.sccomponents.gauges.gr004.GR004;

public class DashboardGenerator extends AppCompatActivity {
    GR004 gauge_rpm,gauge_pressure,gauge_temp,gauge_batv,gauge_fuel;
    TextView text_hours;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_generator);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
}
