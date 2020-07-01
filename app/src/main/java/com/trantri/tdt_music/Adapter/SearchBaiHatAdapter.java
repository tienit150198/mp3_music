package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;

import java.util.List;

public class SearchBaiHatAdapter extends RecyclerView.Adapter<SearchBaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> list;

    public SearchBaiHatAdapter(Context mContext, List<BaiHatYeuThich> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_timkiem_baihat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = list.get(position);
        holder.txtTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());
        holder.txtCasi.setText(baiHatYeuThich.getCaSi());
        Glide.with(mContext).load(baiHatYeuThich.getHinhBaiHat()).placeholder(R.drawable.ic_place_holder).into(holder.imgBaiHat);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBaiHat, imgLike;
        TextView txtTenBaiHat, txtCasi;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBaiHat = itemView.findViewById(R.id.img_timkiem);
            imgLike = itemView.findViewById(R.id.img_SearchLuotThich);
            txtTenBaiHat = itemView.findViewById(R.id.tv_SearchBaiHat);
            txtCasi = itemView.findViewById(R.id.tv_SearchTenCaSi);
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayMusicActivity.class);
            intent.putExtra("cakhuc",new Gson().toJson(list.get(getAdapterPosition())));
            mContext.startActivity(intent);
        });
        }
    }

}
