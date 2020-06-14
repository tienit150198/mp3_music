package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.activity.DanhSachTheLoaiTheoChuDeActivity;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemCacChuDeBinding;

import java.util.List;

public class DanhSachAllChuDeAdapter extends RecyclerView.Adapter<DanhSachAllChuDeAdapter.ViewHolder> {
    List<ChuDe> mList;
    Context mContext;

    public DanhSachAllChuDeAdapter(Context mContext, List<ChuDe> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemCacChuDeBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ChuDe chuDe = mList.get(position);

        Glide.with(mContext)
                .load(chuDe.getHinhChuDe())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.binding.imgAllChuDe);
        holder.binding.imgAllChuDe.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DanhSachTheLoaiTheoChuDeActivity.class);
            intent.putExtra("chude", mList.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCacChuDeBinding binding;
        public ViewHolder(@NonNull ItemCacChuDeBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
