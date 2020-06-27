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
    private List<ChuDe> mList;
    private OnItemClickedListener mListener;
    public DanhSachAllChuDeAdapter(@NonNull List<ChuDe> list, @NonNull OnItemClickedListener listener) {
        mList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemCacChuDeBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mList != null ? mList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCacChuDeBinding binding;
        public ViewHolder(@NonNull ItemCacChuDeBinding b) {
            super(b.getRoot());
            binding = b;

            itemView.setOnClickListener(v -> {
                if(mListener != null){
                    mListener.onItemClicked(mList.get(getAdapterPosition()));
                }
            });
        }

        public void bindData(ChuDe chuDe){
            Glide
                    .with(itemView.getContext())
                    .load(chuDe.getHinhChuDe())
                    .into(binding.imgAllChuDe);
        }
    }

    public interface OnItemClickedListener{
        default void onItemClicked(@NonNull ChuDe chude){};
    }
}
