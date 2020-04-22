package com.twintech.smartcontroller;

import android.os.Build;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;

public class ChannelData {
    private int channelNo;
    private int channelId;
    private String channelName;
    private String channelType;
    private String channelUnit;
    private int channelPrecision;
    private double channelRelayLow;
    private double channelRelayLHigh;
    private double channelAlarmLow;
    private double channelAlarmHigh;
    private String channelValue;
    private String channelDate;
    private String channelTime;

    public ChannelData(int channelNo, String channelName, String channelUnit, String channelValue,String channelDate,String channelTime,Double channelAlarmLow,Double channelAlarmHigh) {
        this.channelNo = channelNo;
        this.channelName = channelName;
        this.channelUnit = channelUnit;
        this.channelValue = channelValue;
        this.channelDate= channelDate;
        this.channelTime= channelTime;
        this.channelAlarmLow= channelAlarmLow;
        this.channelAlarmHigh= channelAlarmHigh;
    }
    ////Get all the values
    public String getchannelNo() {return String.valueOf(channelNo);}
    public String getchannelId() {
        return String.valueOf(channelId);
    }

    public String getchannelName() {        return channelName;    }
    public String getchannelType() {
        return channelType;
    }
    public String getchannelUnit() {
        return channelUnit;
    }
    public String getchannelValue() {
        return channelValue;
    }
    public String getchannelDate() { return channelDate;}
    public String getchannelTime() { return channelTime;}
    public double getchannelRelayLow() { return channelRelayLow; }
    public double getchannelRelayHigh() { return channelRelayLHigh; }
    public double getchannelAlarmLow() { return channelAlarmLow; }
    public double getchannelAlarmHigh() { return channelAlarmHigh; }


    // Set all the value
    public void setchannelValue(String data) {
        this.channelValue = data;
    }
    public void setchannelId(String data) {
        this.channelId = Integer.valueOf(data);
    }
    public void setchannelName(String data) {
        this.channelName = data;
    }
    public void setchannelType(String data) {
        this.channelType = data;
    }
    public void getchannelUnit(String data) {
        this.channelUnit= data;
    }
    public void setchannelDate(String date) { this.channelDate = date; }
    public void setchannelTime(String time) {
        this.channelTime = time;
    }
    public void setchannelRelayLow(String data) {  this.channelRelayLow = Double.valueOf(data); }
    public void setchannelRelayHigh(String data) {  this.channelRelayLHigh = Double.valueOf(data); }
    public void setchannelAlarmLow(String data) {  this.channelAlarmLow = Double.valueOf(data); }
    public void setchannelAlarmHigh(String data) {  this.channelAlarmHigh = Double.valueOf(data); }
}