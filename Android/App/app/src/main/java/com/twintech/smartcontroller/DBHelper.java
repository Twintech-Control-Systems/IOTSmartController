package com.twintech.smartcontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ChannelDB.db";
    public static final String CHANNEL_LIST_TABLE_NAME = "ChannelList";
    public static final String CHANNEL_COLUMN_NO = "number";
    public static final String CHANNEL_COLUMN_ID = "id";
    public static final String CHANNEL_COLUMN_TYPE = "type";
    public static final String CHANNEL_COLUMN_NAME = "name";
    public static final String CHANNEL_COLUMN_VALUE = "value";
    public static final String CHANNEL_COLUMN_UNIT = "unit";
    public static final String CHANNEL_COLUMN_PREC = "precision";
    public static final String CHANNEL_COLUMN_RLOW = "rangelow";
    public static final String CHANNEL_COLUMN_RHIGH = "rangehigh";
    public static final String CHANNEL_COLUMN_RLYLOW = "relaylow";
    public static final String CHANNEL_COLUMN_RLYHIGH = "relayhigh";
    public static final String CHANNEL_COLUMN_ALMLOW = "alarmlow";
    public static final String CHANNEL_COLUMN_ALMHIGH = "alarmhigh";
    public static final String CHANNEL_COLUMN_DATE = "date";
    public static final String CHANNEL_COLUMN_TIME = "time";



    public static final String CHANNEL_VALUES_TABLE_NAME = "ChannelValues";
    public static final String CHANNEL1_COLUMN_NO = "number";
    public static final String CHANNEL1_COLUMN_ID = "id";
    public static final String CHANNEL1_COLUMN_NAME = "name";
    public static final String CHANNEL1_COLUMN_TYPE = "type";
    public static final String CHANNEL1_COLUMN_UNIT = "unit";
    public static final String CHANNEL1_COLUMN_VALUE = "value";
    public static final String CHANNEL1_COLUMN_DATE = "date";
    public static final String CHANNEL1_COLUMN_TIME = "time";


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS "+ CHANNEL_LIST_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS "+ CHANNEL_VALUES_TABLE_NAME);
        db.execSQL(
                "create table " + CHANNEL_LIST_TABLE_NAME  +
                        "(number integer primary key, id text,name text,type text,value text, unit text,date text,time text,precision text,"+
                        "rangelow text,rangehigh text,relaylow text,relay high,alarmlow text,alarmhigh text)"
        );
        db.execSQL(
                "create table " + CHANNEL_VALUES_TABLE_NAME  +
                        "(number integer, id text,name text,type text, unit text,value text" +
                        ",date text,time text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ CHANNEL_VALUES_TABLE_NAME);
        onCreate(db);
    }

    //// Insert values in datalog table
    public boolean insertChannelValue (List<ChannelData> mChannelListDB,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", mChannelListDB.get(position).getchannelNo());
        contentValues.put("id", mChannelListDB.get(position).getchannelId());
        contentValues.put("name", mChannelListDB.get(position).getchannelName());
        contentValues.put("type", mChannelListDB.get(position).getchannelType());
        contentValues.put("unit", mChannelListDB.get(position).getchannelUnit());
        contentValues.put("value", mChannelListDB.get(position).getchannelValue());
        contentValues.put("date", mChannelListDB.get(position).getchannelDate());
        contentValues.put("time", mChannelListDB.get(position).getchannelTime());
        db.insert(CHANNEL_VALUES_TABLE_NAME, null, contentValues);
        return true;
    }
    /// create a channel values for the first time.
    public boolean createChannelValue (List<ChannelData> mChannelListDB,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", mChannelListDB.get(position).getchannelNo());
        contentValues.put("id", mChannelListDB.get(position).getchannelId());
        contentValues.put("name", mChannelListDB.get(position).getchannelName());
        contentValues.put("type", mChannelListDB.get(position).getchannelType());
        contentValues.put("unit", mChannelListDB.get(position).getchannelUnit());
        contentValues.put("value", mChannelListDB.get(position).getchannelValue());
        contentValues.put("date", mChannelListDB.get(position).getchannelDate());
        contentValues.put("time", mChannelListDB.get(position).getchannelTime());
        contentValues.put("alarmlow", mChannelListDB.get(position).getchannelAlarmLow());
        contentValues.put("alarmhigh", mChannelListDB.get(position).getchannelAlarmHigh());
        db.insert(CHANNEL_LIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getCurrentChannelValue (int channelNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CHANNEL_LIST_TABLE_NAME + " where number=" + String.valueOf(channelNo+1)+" ", null );
        return res;
    }
    public Cursor getData(int channelNo,String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + tableName + " where number="+String.valueOf(channelNo)+" ", null );
        return res;
    }

    public int numberOfRows(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, tableName);
        return numRows;
    }

    public boolean updateChannelValue (List<ChannelData> mChannelListDB,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", mChannelListDB.get(position).getchannelValue());
        contentValues.put("date", mChannelListDB.get(position).getchannelDate());
        contentValues.put("time", mChannelListDB.get(position).getchannelTime());
        db.update(CHANNEL_LIST_TABLE_NAME, contentValues, "number = ? ", new String[] { Integer.toString(position+1) } );
        return true;
    }
    public boolean addDataLog (List<ChannelData> mChannelListDB,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", mChannelListDB.get(position).getchannelNo());
        contentValues.put("id", mChannelListDB.get(position).getchannelId());
        contentValues.put("name", mChannelListDB.get(position).getchannelName());
        contentValues.put("type", mChannelListDB.get(position).getchannelType());
        contentValues.put("unit", mChannelListDB.get(position).getchannelUnit());
        contentValues.put("value", mChannelListDB.get(position).getchannelValue());
        contentValues.put("date", mChannelListDB.get(position).getchannelDate());
        contentValues.put("time", mChannelListDB.get(position).getchannelTime());
        db.insert(CHANNEL_VALUES_TABLE_NAME, null, contentValues);;
        return true;
    }
    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ChannelData> getAllData() {
        ArrayList<ChannelData> array_list = new ArrayList<ChannelData>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CHANNEL_VALUES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            //array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}