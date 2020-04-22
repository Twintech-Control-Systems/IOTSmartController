package com.twintech.smartcontroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class dashboard extends AppCompatActivity {
    private int noOfChannels = 8;
    private int channelScanPeriod = 1000;
    DashboardAdapter myAdapter;
    ImageButton notificationButton,graphButton,alarmButton,refreshButton,wifiButton,cloudButton,settingsButton;
    private static final int DEFAULT_PORT = 8080;
    RecyclerView mRecyclerView;
    public List<ChannelData> mChannelList,sChannelList;
    ChannelData mChannelData;
    // INSTANCE OF ANDROID WEB SERVER
    private AndroidWebServer androidWebServer;

    private static boolean relayNotification = true;
    private Timer myTimer;
    DBHelper mydb;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try{
            //some exception
        }catch(Exception e){

            Log.e("",e.getMessage());
        }
        mydb = new DBHelper(this);
        addChannels();
        initChannels();
        startService(new Intent(getBaseContext(), ChannelService.class));

        mRecyclerView = findViewById(R.id.recyclerview);
        /// Recycler view to grid lyout
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(dashboard.this, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // Add Channel Data
        //getDataFromDB();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                //getChannelDataHW();
                getDataFromDB();
                runOnUiThread(new Runnable() {
                    public void run() {
                        myAdapter.dataSetChanged(myAdapter);


                    }
                });
            }

        }, 0, channelScanPeriod);
        myAdapter = new DashboardAdapter(dashboard.this, mChannelList);
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
                    break;
                case R.id.graphButton:
                    Toast.makeText(getApplicationContext(),
                            "Graph button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.alarmButton:
                    Toast.makeText(getApplicationContext(),
                            "Alarm button pressed!!", Toast.LENGTH_SHORT).show();
                    displayDialog("Dialog !!");
                    break;
                case R.id.refreshButton:
                    Toast.makeText(getApplicationContext(),
                            "Refresh button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settingsButton:
                    Toast.makeText(getApplicationContext(),
                            "Settings button pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), ChannelService.class));

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
       myAdapter.dataSetChanged(myAdapter);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // get data from database
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void getDataFromDB(){
        for(int i=0;i<noOfChannels;i++){
            Cursor rs = mydb.getCurrentChannelValue(i);
            rs.moveToFirst();
            mChannelList.get(i).setchannelValue(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_VALUE)));
            mChannelList.get(i).setchannelDate(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_DATE)));
            mChannelList.get(i).setchannelTime(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_TIME)));
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Create channel Array List and Channel data for the first time and add to thr database
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void addChannels(){
        mChannelList = new ArrayList<>();
       /* mChannelData = new ChannelData(1, "Channel 1","%rh","056.8","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(2, "Channel 2","°C","124.9","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(3, "Channel 3","°F","212.0","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(4, "Channel 4","bar","600.0","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(5, "Channel 5","Kg","100.0","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(6, "Channel 6","W","5600","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(7, "Channel 7","V","230.0","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        mChannelData = new ChannelData(8, "Channel 8","A","10.6","20-04-2020","16:45:00",20.0,80.0);
        mChannelList.add(mChannelData);
        Log.d("MyTag",String.valueOf(mChannelList.get(0).getchannelAlarmHigh()));
        for (int i = 0; i < noOfChannels; i++) { mydb.createChannelValue(mChannelList,i); }*/
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialize the data
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void initChannels(){
        for(int i=0;i<noOfChannels;i++){
            Cursor rs = mydb.getCurrentChannelValue(i);
            rs.moveToFirst();
            mChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_NO)),
                    rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_NAME)),
                    rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_UNIT)),
                    rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_VALUE)),
                    rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_DATE)),
                    rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_TIME)),
                    Double.valueOf(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_ALMLOW))),
                            Double.valueOf(rs.getString(rs.getColumnIndex(DBHelper.CHANNEL_COLUMN_ALMHIGH))));
            mChannelList.add(mChannelData);
        }
     }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), ChannelService.class));
    }

    // Method to stop the service
    public void stopService(View view) {

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void getChannelDataHW() {
        String vChannelValue;
        Random random = new Random();
        for (int i = 0; i < noOfChannels; i++) {
            double randomNumber = random.nextInt(1000) / 10;
            vChannelValue = String.format("%.1f", randomNumber);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            String time = formattedDate;
            df = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = df.format(c.getTime());
            String date = formattedDate;
            mChannelList.get(i).setchannelValue(String.valueOf(vChannelValue));
            mChannelList.get(i).setchannelTime(time);
            mChannelList.get(i).setchannelDate(date);

            mydb.updateChannelValue(mChannelList,i);
            //Log.d("MyTag",mChannelList.get(i).getchannelName());
            //mydb.addDataLog(mChannelList,i);
        }
    }

    void displayDialog(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(dashboard.this,"You clicked yesbutton",Toast.LENGTH_LONG).show();
                            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
