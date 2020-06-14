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
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemTheLoaiTheoChuDeBinding;

import java.util.List;

public class DanhSachTheLoaiTheoChuDeAdapter extends RecyclerView.Adapter<DanhSachTheLoaiTheoChuDeAdapter.ViewHolder> {
    List<TheLoai> mTheLoais;

    public DanhSachTheLoaiTheoChuDeAdapter(List<TheLoai> mTheLoais) {
        this.mTheLoais = mTheLoais;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemTheLoaiTheoChuDeBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheLoai theLoai = mTheLoais.get(position);
        Glide.with(holder.binding.imgTheloaitheochude)
                .load(theLoai.getHinhTheLoai())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.binding.imgTheloaitheochude);
        holder.binding.tvTheloaitheochude.setText(theLoai.getTenTheLoai());
    }

    @Override
    public int getItemCount() {
        return mTheLoais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTheLoaiTheoChuDeBinding binding;

        public ViewHolder(@NonNull ItemTheLoaiTheoChuDeBinding b) {
            super(b.getRoot());
            binding = b;
            binding.lnChude.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SongsListActivity.class);
                intent.putExtra("idtheloai", mTheLoais.get(getLayoutPosition()));
                v.getContext().startActivity(intent);
            });
        }

    }
}

