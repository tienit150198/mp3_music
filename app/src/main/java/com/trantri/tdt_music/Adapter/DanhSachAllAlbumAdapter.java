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
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.R;

import java.util.List;

public class DanhSachAllAlbumAdapter extends RecyclerView.Adapter<DanhSachAllAlbumAdapter.ViewHolder> {
    Context mContext;
    List<Album> albumList;

    public DanhSachAllAlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_all_album, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        Glide.with(mContext).load(album.getHinhAlbum()).placeholder(R.drawable.ic_place_holder).into(holder.imgAllAlbum);
        holder.txtAllAlbum.setText(album.getTenAlbum());
        holder.txtTenCaSi.setText(album.getTenCaSiAlbum());

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAllAlbum;
        TextView txtAllAlbum, txtTenCaSi;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgAllAlbum = itemView.findViewById(R.id.img_allAlbum);
            txtAllAlbum = itemView.findViewById(R.id.tv_tenAllAlbum);
            txtTenCaSi = itemView.findViewById(R.id.tv_tenCaSiAlBum);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(mContext, SongsListActivity.class);
                        intent.putExtra("album", albumList.get(getPosition()));
                        mContext.startActivity(intent);
                 }
             });
        }
    }
}
