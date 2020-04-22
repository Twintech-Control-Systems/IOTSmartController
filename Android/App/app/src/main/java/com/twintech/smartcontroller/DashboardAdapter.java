package com.twintech.smartcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import static com.twintech.smartcontroller.R.*;
import static com.twintech.smartcontroller.R.layout.recyclerview_channel_layout;
import static java.lang.Double.*;

public class DashboardAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private Context mContext;
    private List<ChannelData> mChannelList;

    DashboardAdapter(Context mContext, List<ChannelData> mFlowerList) {
        this.mContext = mContext;
        this.mChannelList = mFlowerList;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView;
        mView = LayoutInflater.from(parent.getContext()).inflate(recyclerview_channel_layout, parent, false);
        return new ChannelViewHolder(mView);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final ChannelViewHolder holder, int position) {
        //holder.mChannelNo.setText("Channel No " + String.valueOf(mChannelList.get(position).getchannelNo()));
        holder.mChannelName.setText(mChannelList.get(position).getchannelName());
        holder.mChannelValue.setText(mChannelList.get(position).getchannelValue());
        holder.mChannelUnit.setText(mChannelList.get(position).getchannelUnit());

        if( parseDouble(mChannelList.get(position).getchannelValue()) > mChannelList.get(position).getchannelAlarmHigh()){
            holder.mCardView.setCardBackgroundColor(Color.RED);
            holder.mChannelValue.setTextColor(Color.BLACK);
        }else
        if( parseDouble(mChannelList.get(position).getchannelValue()) < mChannelList.get(position).getchannelAlarmLow()){
            holder.mCardView.setCardBackgroundColor(Color.LTGRAY);
            holder.mChannelValue.setTextColor(Color.GREEN);
        }else{
            holder.mCardView.setCardBackgroundColor(Color.LTGRAY);
            holder.mChannelValue.setTextColor(Color.YELLOW);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, DashboardDetail.class);
                mIntent.putExtra("Number", mChannelList.get(holder.getAdapterPosition()).getchannelNo());
                mIntent.putExtra("Name", mChannelList.get(holder.getAdapterPosition()).getchannelName());
               mIntent.putExtra("Value", mChannelList.get(holder.getAdapterPosition()).getchannelValue());
               mIntent.putExtra("Unit", mChannelList.get(holder.getAdapterPosition()).getchannelUnit());
                mContext.startActivity(mIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
       return mChannelList.size();
        //return 0;
    }

    public void dataSetChanged(DashboardAdapter mAdapter){

        mAdapter.notifyDataSetChanged();
    }

}

class ChannelViewHolder extends RecyclerView.ViewHolder {
   TextView mChannelNo,mChannelName,mChannelValue,mChannelUnit;
    CardView mCardView;
    ChannelViewHolder(View itemView) {
        super(itemView);
       // mChannelNo = itemView.findViewById(R.id.channelNo);
        mChannelName = itemView.findViewById(id.channelName);
        mChannelValue = itemView.findViewById(id.channelValue);
        mChannelUnit = itemView.findViewById(id.channelUnit);
        mCardView = itemView.findViewById(id.cardview);
        mChannelName.setTextColor(Color.BLACK);
        mChannelValue.setTextColor(Color.BLACK);
        mChannelUnit.setTextColor(Color.BLACK);
    }

}