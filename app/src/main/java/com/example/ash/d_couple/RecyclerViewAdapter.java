package com.example.ash.d_couple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by ash on 2016-11-06.
 * recycler adapter 리싸이클러 뷰에 클래스를 적용하기 위함
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{
    ArrayList<DesignerData> itemDatas;
    View.OnClickListener clickEvent;
    RequestManager mRequestManager;
    public RecyclerViewAdapter(ArrayList<DesignerData> itemDatas, View.OnClickListener clickEvent,RequestManager requestManager) {
        this.itemDatas = itemDatas;
        this.clickEvent = clickEvent;
        this.mRequestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(clickEvent);
        return viewHolder;
    }

    //이미지를 서버에서 받아와 출력 id,interest 출력
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mRequestManager.load(itemDatas.get(position).getImageURL()).into(holder.imageView); //glide적용
        holder.textViewID.setText(itemDatas.get(position).getID());
        holder.textViewInterest.setText(itemDatas.get(position).getInterest());
    }

    @Override
    public int getItemCount() {
        return (itemDatas != null) ? itemDatas.size() : 0;
    }

}