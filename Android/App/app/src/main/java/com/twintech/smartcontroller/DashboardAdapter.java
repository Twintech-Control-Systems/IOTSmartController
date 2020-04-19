package com.twintech.smartcontroller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import static com.twintech.smartcontroller.R.layout.recyclerview_channel_layout;

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

    @Override
    public void onBindViewHolder(final ChannelViewHolder holder, int position) {
        //holder.mChannelNo.setText("Channel No " + String.valueOf(mChannelList.get(position).getchannelNo()));
        holder.mChannelName.setText(mChannelList.get(position).getchannelName());
        holder.mChannelValue.setText(mChannelList.get(position).getchannelValue());
        holder.mChannelUnit.setText(mChannelList.get(position).getchannelUnit());


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, DashboardDetail.class);
                //mIntent.putExtra("Title", mFlowerList.get(holder.getAdapterPosition()).getFlowerName());
               // mIntent.putExtra("Description", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription());
                //mIntent.putExtra("Image", mFlowerList.get(holder.getAdapterPosition()).getFlowerImage());
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }
}

class ChannelViewHolder extends RecyclerView.ViewHolder {
   TextView mChannelNo,mChannelName,mChannelValue,mChannelUnit;
    CardView mCardView;
    ChannelViewHolder(View itemView) {
        super(itemView);
       // mChannelNo = itemView.findViewById(R.id.channelNo);
        mChannelName = itemView.findViewById(R.id.channelName);
        mChannelValue = itemView.findViewById(R.id.channelValue);
        mChannelUnit = itemView.findViewById(R.id.channelUnit);
        mCardView = itemView.findViewById(R.id.cardview);
    }
}