package com.example.ash.d_couple;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ash on 2016-11-06.
 * ViewHolder 아이템을 담을 클래스
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    TextView textViewID;
    TextView textViewInterest;
    ImageView imageView;
    public ViewHolder(View itemView) {
        super(itemView);
        textViewID = (TextView)itemView.findViewById(R.id.recyclerID);
        textViewInterest = (TextView)itemView.findViewById(R.id.interestArea2);
        imageView = (ImageView)itemView.findViewById(R.id.imageView1);
    }
}