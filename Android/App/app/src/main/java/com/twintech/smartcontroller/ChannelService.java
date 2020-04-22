package com.twintech.smartcontroller;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;



public class ChannelService extends Service {
    private int noOfChannels = 8;
    private int channelScanPeriod = 5000;
    private Timer myTimer;
    private static boolean isStarted = false;
    ////////////////////////////////////////////////////////////
    public List<ChannelData> mChannelList,sChannelList;
    ChannelData mChannelData;
    ///////////////////////////////////////////////////////////
    DBHelper mydb;
    //////////////////////////////////////////////////////////
    private static final int DEFAULT_PORT = 8080;
    private AndroidWebServer androidWebServer;

    @Override
    public void onCreate() {
        mydb = new DBHelper(this);
        initChannels();
        startAndroidWebServer();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.d("MyTag","Service Started");
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                getChannelDataHW();
            }

        }, 0, channelScanPeriod);

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        androidWebServer.stop();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
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
            //mydb.addDataLog(mChannelList,i);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialize the data
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void initChannels(){
        mChannelList = new ArrayList<>();
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
            //Log.d("MyTag",mChannelData.getchannelValue());
            mChannelList.add(mChannelData);
        }
    }

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
}