package com.twintech.smartcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsMain extends AppCompatActivity {
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        prepareListData();
        expListView = findViewById(R.id.lvExp);
        listAdapter = new com.twintech.smartcontroller.ExpandableListAdapter(this,listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);
        ArrayList<String> arrayList = new ArrayList<>();
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /////////// Get settings.json File   ///////////////////////////////////

        try {
            final InputStream open = getAssets().open("settings.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {

                if(groupPosition==0){
                    if(childPosition == 0)
                    {
                        addFragment(new DeviceFragment(),false,"one");
                    }
                    if(childPosition == 1)
                    {
                        addFragment(new ChannelFragment(),false,"one");
                    }
                    if(childPosition == 2)
                    {
                        addFragment(new RelayFragment(),false,"one");
                    }
                    if(childPosition == 3)
                    {
                        addFragment(new DatalogFragment(),false,"one");
                    }
                    if(childPosition == 4)
                    {

                    }
                }
                if(groupPosition==1){

                }
                return false;
            }
            private void addFragment(Fragment fragment, boolean addToBackStack, String tag)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();

                if (addToBackStack) {
                    ft.addToBackStack(tag);
                }
                ft.replace(R.id.frame_container, fragment, tag);
                ft.commitAllowingStateLoss();
            }
        });
        /*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                i=0;
                String clickedItem=(String) list.getItemAtPosition(i);
                if(i==0)
                {
                    addFragment(new FragmentWifi(),false,"one");
                }
                if(i==1)
                {
                    //addFragment(new FragmentTwo(),false,"two");
                }
                if(i==2)
                {
                    //addFragment(new FragmentThree(),false,"two");
                }
                if(i==3)
                {
                    //addFragment(new FragmentFour(),false,"two");
                }
                if(i==4)
                {
                    //addFragment(new FragmentFive(),false,"two");
                }
                if(i==5)
                {
                    //addFragment(new FragmentSix(),false,"two");
                }
                Toast.makeText(SettingsMain.this,clickedItem,Toast.LENGTH_LONG).show();

            }


        });*/
        ////////////////////////View Page Adapter//////////////////////////////////////////////////////

    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Hardware");
        listDataHeader.add("Server");
        listDataHeader.add("Device");
        listDataHeader.add("About");
        listDataHeader.add("Software Update");
        listDataHeader.add("Factory Reset");

        // Adding child data
        List<String> hardware = new ArrayList<String>();
        hardware.add("Device");
        hardware.add("Channel");
        hardware.add("Relay");
        hardware.add("Data Log");
        hardware.add("Calibration");
        hardware.add("Dashboard");

        List<String> server = new ArrayList<String>();
        server.add("Internal");
        server.add("Cloud");
        server.add("Email");
        server.add("Google Drive");

        List<String> device = new ArrayList<String>();
        device.add("WiFi");
        device.add("GSM");
        device.add("Location");
        device.add("Date and Time");
        device.add("Display");
        device.add("Sound");
        device.add("Lock");

        listDataChild.put(listDataHeader.get(0), hardware); // Header, Child data
        listDataChild.put(listDataHeader.get(1), server);
        listDataChild.put(listDataHeader.get(2), device);
    }
}
