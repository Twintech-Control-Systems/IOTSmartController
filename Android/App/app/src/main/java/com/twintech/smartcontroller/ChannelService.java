package com.twintech.smartcontroller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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




public class ChannelService extends Service  implements SensorEventListener{
    private int noOfChannels = 8;
    private int channelScanPeriod = 1000;
    private Timer myTimer;
    private static boolean isStarted = false;
    SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private Sensor mSensorLight;
    ArrayList<Double> sensorArray = new ArrayList<>();
    ////////////////////////////////////////////////////////////
    public List<ChannelData> mChannelList,sChannelList;
    ChannelData mChannelData;
    ///////////////////////////////////////////////////////////
    DBHelper mydb;
    //////////////////////////////////////////////////////////
    private static final int DEFAULT_PORT = 8080;
    private AndroidWebServer androidWebServer;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        sensorArray.add(0,0.0);
        sensorArray.add(1,0.0);
        sensorArray.add(2,0.0);
        sensorArray.add(3,0.0);
        mydb = new DBHelper(this);
        initSensors();
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
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void getChannelDataHW() {

        String vChannelValue;
        Random random = new Random();/*
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
            mydb.addDataLog(mChannelList,i);
        }
        */
        for (int i = 0; i < 4; i++) {
            double randomNumber = sensorArray.get(i).doubleValue();
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
            mydb.addDataLog(mChannelList,i);
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Init Sensors
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void initSensors(){
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mSensorAcc != null) {
            mSensorManager.registerListener(this, mSensorAcc,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            getLight(sensorEvent);
        }

    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        sensorArray.add(0, (double) values[0]);
        sensorArray.add(1, (double) values[1]);
        sensorArray.add(2, (double) values[2]);
    }
    private void getLight(SensorEvent event) {
        float[] values = event.values;
        // Movement
        sensorArray.add(3, (double) values[0]);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}