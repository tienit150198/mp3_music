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
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemAlbumBinding;
import com.trantri.tdt_music.databinding.ItemAllAlbumBinding;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    Context mContext;
    List<Album> albumList;

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(ItemAlbumBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.binding.tvTenAlbum.setText(album.getTenAlbum());
        holder.binding.tvTenCaSiALbum.setText(album.getTenCaSiAlbum());
        Glide.with(mContext)
                .load(album.getHinhAlbum())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.binding.imgAlbum);
    }

    @Override
    public int getItemCount() {
        return albumList.isEmpty() ? 0 : albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAlbumBinding binding;

        public ViewHolder(@NonNull ItemAlbumBinding b) {
            super(b.getRoot());
            binding = b;
            binding.ClickAlbum.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SongsListActivity.class);
                intent.putExtra("album", albumList.get(getLayoutPosition()));
                mContext.startActivity(intent);
            });
        }
    }
}
