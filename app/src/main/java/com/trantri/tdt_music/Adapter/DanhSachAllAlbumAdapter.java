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
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemAlbumBinding;
import com.trantri.tdt_music.databinding.ItemAllAlbumBinding;

import java.util.List;

public class DanhSachAllAlbumAdapter extends RecyclerView.Adapter<DanhSachAllAlbumAdapter.ViewHolder> {

    List<Album> albumList;


    public DanhSachAllAlbumAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemAllAlbumBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.binding.imgAllAlbum)
                .load(albumList.get(position).getHinhAlbum())
                .error(R.drawable.ic_place_holder)
                .into(holder.binding.imgAllAlbum);
        holder.binding.tvTenAllAlbum.setText(albumList.get(position).getTenAlbum());
        holder.binding.tvTenCaSiAlBum.setText(albumList.get(position).getTenCaSiAlbum());

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAllAlbumBinding binding;

        ViewHolder(ItemAllAlbumBinding b) {
            super(b.getRoot());
            binding = b;
            binding.lnClick.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SongsListActivity.class);
                intent.putExtra("album", albumList.get(getLayoutPosition()));
                v.getContext().startActivity(intent);
            });
        }
    }
}
