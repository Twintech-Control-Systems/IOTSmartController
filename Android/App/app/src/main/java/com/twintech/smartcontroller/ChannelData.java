package com.twintech.smartcontroller;

public class ChannelData {
    private int channelNo;
    private int channelId;
    private String channelName;
    private String channelType;
    private String channelUnit;
    private int channelPrecision;
    private double channelRangeLow;
    private double channelRangeLHigh;
    private double channelSectorLow;
    private double channelSectorMed;
    private double channelSectorHigh;
    private String channelValue;

    public ChannelData(int channelNo, String channelName, String channelUnit,String channelValue) {
       this.channelNo = channelNo;
       this.channelName = channelName;
       this.channelUnit = channelUnit;
        this.channelValue = channelValue;
    }

    public int getchannelNo() {
        return channelNo;
    }

    public String getchannelName() {
        return channelName;
    }

    public String getchannelUnit() {
        return channelUnit;
    }

    public String getchannelValue() {
        return channelValue;
    }
}