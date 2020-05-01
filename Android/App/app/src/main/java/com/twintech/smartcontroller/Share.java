package com.twintech.smartcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Double.*;

public class Share extends AppCompatActivity {
    Chip chip1,chip2,chip3,chip4,chip5,chip6,chip7,chip8;
    Button exportButton,share1Button,fromDate,toDate;
    TextView textview;
    DBHelper mydb;
    ProgressBar progressBar;
    private DatePickerDialog.OnDateSetListener fromDateSetListener,toDateSetListener;
    public List<ChannelData> mChannelList = new ArrayList<>();
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //////////// Initialize the chips
        chip1 = findViewById(R.id.channel1Chip);
        chip2 = findViewById(R.id.channel2Chip);
        chip3 = findViewById(R.id.channel3Chip);
        chip4 = findViewById(R.id.channel4Chip);
        chip5 = findViewById(R.id.channel5Chip);
        chip6 = findViewById(R.id.channel6Chip);
        chip7 = findViewById(R.id.channel7Chip);
        chip8 = findViewById(R.id.channel8Chip);
        //////////// Init Buttons
        exportButton = findViewById(R.id.exportDataButton);
        share1Button = findViewById(R.id.share1Button);
        fromDate = findViewById(R.id.fromDataButton);
        toDate = findViewById(R.id.toDateButton);
        textview = findViewById(R.id.textView);
        //////////// Set on Click listener to all the buttons
        exportButton.setOnClickListener(buttonListener);
        share1Button.setOnClickListener(buttonListener);
        fromDate.setOnClickListener(buttonListener);
        toDate.setOnClickListener(buttonListener);
        progressBar = findViewById(R.id.progressBar);
        ArrayList<String> listItems=new ArrayList<String>();
        ListView listView = findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        SensorManager sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        List<Sensor> list = sMgr.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: list){
            adapter.add(sensor.getName());
        }
        //////////// init DB
        mydb = new DBHelper(this);
        //////////// Init data
        progressBar.setVisibility(View.INVISIBLE);
        fromDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet (DatePicker datePicker,int year, int month, int day){
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                fromDate.setText(date);
            }
        };
         toDateSetListener= new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet (DatePicker datePicker,int year, int month, int day){
                month = month + 1;
               String date = day + "/" + month + "/" + year;
                toDate.setText(date);
            }
        };
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            // So we will make
            switch (v.getId() /*to get clicked view id**/) {
                case R.id.exportDataButton:
                    View contextView = findViewById(R.id.exportDataButton);
                    Snackbar.make(contextView, "Export Data Button Pressed!!", Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        public void run() {

                            //progressBar.();
                            try {
                                exportData(getDataFromDB());
                            } catch (IOException e) {
                                Log.d("Error",e.getMessage());
                            }
                        }
                    }).start();
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                case R.id.share1Button:
                    Toast.makeText(getApplicationContext(),
                            "Share button pressed!!", Toast.LENGTH_SHORT).show();
                    shareFiles();
                    break;
                case R.id.fromDataButton:
                    DatePickerDialog dialog = new DatePickerDialog(
                            Share.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            fromDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    break;
                case R.id.toDateButton:
                    DatePickerDialog dialog1 = new DatePickerDialog(
                            Share.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            toDateSetListener,
                            year,month,day);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.show();
                    break;
                default:
                    break;
            }
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // shoe date picker dialog box
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Export Data
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void exportData(ArrayList<ChannelData> exportChannelData) throws IOException {
        /*
        if (ContextCompat.checkSelfPermission(Share.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        */
        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("yourtag", "Error Package name not found ", e);
        }
        //String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("Error",s +"/files"+ File.separator + "test.csv");
        String fileName = "test.csv";
        File f = new File(s +"/files"+ File.separator + fileName);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        //int channelNo, String channelName, String channelUnit, String channelValue,String channelDate,String channelTime,Double channelAlarmLow,Double channelAlarmHigh
        bw.write("Channel No,Id,Name,Unit,Value,Date,Time");
        bw.newLine();
        for(int i=0;i<exportChannelData.size();i++)
        {
            bw.write(String.valueOf(exportChannelData.get(i).getchannelNo())+",");
            bw.write(exportChannelData.get(i).getchannelId()+",");
            bw.write(exportChannelData.get(i).getchannelName()+",");
            bw.write(exportChannelData.get(i).getchannelUnit()+",");
            bw.write(exportChannelData.get(i).getchannelValue()+",");
            bw.write(exportChannelData.get(i).getchannelDate()+",");
            bw.write(exportChannelData.get(i).getchannelTime()+",");
            bw.newLine();
        }
        bw.close();
        fw.close();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // get data from database
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ArrayList<ChannelData> getDataFromDB(){
        ChannelData exportChannelData;
        ArrayList<ChannelData> exportChannelList;
        exportChannelList = new ArrayList<>();

        ///////////////////////////////////////// Channel 1 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(1,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
            textview.setText(String.valueOf(exportChannelList.get(1).getchannelUnit()));
        }

        ///////////////////////////////////////// Channel 2 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(2,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 3 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(3,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 4 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(4,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 5 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(5,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 6 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(6,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 7 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(7,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        ///////////////////////////////////////// Channel 8 ////////////////////////////////////////
        if(chip1.isChecked()){
            Cursor rs = mydb.getData(8,"ChannelValues");
            rs.moveToFirst();
            while(rs.isAfterLast() == false){
                exportChannelData = new ChannelData(rs.getInt(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NO)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_NAME)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_UNIT)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_VALUE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_DATE)),
                        rs.getString(rs.getColumnIndex(DBHelper.CHANNEL1_COLUMN_TIME)),
                        0.0,
                        0.0);
                exportChannelList.add(exportChannelData);
                rs.moveToNext();
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        return exportChannelList;
    }

    void shareFiles(){
        PackageManager m = getPackageManager();
        String baseDir = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(baseDir, 0);
            baseDir = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Share", "Error Package name not found ", e);
        }
        String fileName = "test.csv";
        String myFilePath = baseDir + File.separator+"files"+File.separator + fileName;
        File f = new File(myFilePath);
        File fileWithinMyDir = new File(baseDir);
        /*
        Log.d("Files", "Path: " + baseDir);
        File directory = new File(baseDir);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

         */
        if(fileWithinMyDir.exists()) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("text/*");
            Uri photoURI = FileProvider.getUriForFile(this,  BuildConfig.APPLICATION_ID + ".provider", f);
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }

    }
}
