package com.twintech.smartcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    GridView gridView;
    ImageButton notificationButton,graphButton,alarmButton,refreshButton,wifiButton,cloudButton,settingsButton;
    static final String[] numbers = new String[] {
            "Channel 1", "Channel 2", "Channel 3", "Channel 4",
            "Channel 5", "Channel 6", "Channel 7", "Channel 8",
            "Channel 9", "Channel 10", "Channel 11", "Channel 12",
            "Channel 13", "Channel 14", "Channel 15", "Channel 16",
            };
    private static final int DEFAULT_PORT = 8080;
    RecyclerView mRecyclerView;
    List<ChannelData> mChannelList;
    ChannelData mChannelData;
    // INSTANCE OF ANDROID WEB SERVER
    private AndroidWebServer androidWebServer;
    private static boolean isStarted = false;
    private static boolean relayNotification = true;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startAndroidWebServer();
        mRecyclerView = findViewById(R.id.recyclerview);
        /// Recycler view to grid lyout
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(dashboard.this, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        // Add Channel Data
        getChannels();


        DashboardAdapter myAdapter = new DashboardAdapter(dashboard.this, mChannelList);
        mRecyclerView.setAdapter(myAdapter);


        notificationButton = findViewById(R.id.notificationButton);
        if(relayNotification) {
            notificationButton.setImageResource(R.drawable.ic_notification_important_24px);
        }else{
            notificationButton.setImageResource(R.drawable.ic_notifications_24px);
        }
        graphButton = findViewById(R.id.graphButton);
        alarmButton = findViewById(R.id.alarmButton);
        refreshButton = findViewById(R.id.refreshButton);
        settingsButton = findViewById(R.id.settingsButton);
        //////////// Set on Click listener to all the buttons
        notificationButton.setOnClickListener(buttonListener);
        graphButton.setOnClickListener(buttonListener);
        alarmButton.setOnClickListener(buttonListener);
        refreshButton.setOnClickListener(buttonListener);
        settingsButton.setOnClickListener(buttonListener);

    }
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know

            // So we will make
            switch (v.getId() /*to get clicked view id**/) {
                case R.id.notificationButton:
                    View contextView = findViewById(R.id.notificationButton);

                    Snackbar.make(contextView, "Notification Button Pressed!!", Snackbar.LENGTH_SHORT).show();
                    // do something when the corky is clicked

                    break;
                case R.id.graphButton:

                    // do something when the corky2 is clicked
                    Toast.makeText(getApplicationContext(),
                            "Graph button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.alarmButton:

                    // do something when the corky3 is clicked
                    Toast.makeText(getApplicationContext(),
                            "Alarm button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.refreshButton:

                    // do something when the corky3 is clicked
                    Toast.makeText(getApplicationContext(),
                            "Refresh button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settingsButton:

                    // do something when the corky3 is clicked
                    Toast.makeText(getApplicationContext(),
                            "Settings button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private boolean startAndroidWebServer() {
        if (!isStarted) {
            int port = DEFAULT_PORT;
            try {
                if (port == 0) {
                    throw new Exception();
                }
                androidWebServer = new AndroidWebServer(null,port);
                androidWebServer.start();
                return true;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        androidWebServer.stop();
    }

    void getChannels(){
        mChannelList = new ArrayList<>();
        mChannelData = new ChannelData(1, "Channel 1","%rh","056.8");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(2, "Channel 2","°C","124.9");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(3, "Channel 3","°F","212.0");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(4, "Channel 4","bar","600.0");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(5, "Channel 5","Kg","100.0");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(6, "Channel 6","W","5600");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(7, "Channel 7","V","230.0");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(8, "Channel 8","A","10.6");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(9, "Channel 9","Hz","50000");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(10, "Channel 10","RPM","1800");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(11, "Channel 11","%","50.6");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(12, "Channel 12","Hours","999999.9");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(13, "Channel 13","Km/H","15.2");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(14, "Channel 14","W","5600");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(15, "Channel 15","V","230.0");
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(16, "Channel 16","A","10.6");
        mChannelList.add(mChannelData);
    }
}
