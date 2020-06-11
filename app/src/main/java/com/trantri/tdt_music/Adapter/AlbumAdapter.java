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
        View mView = inflater.inflate(R.layout.item_album, parent, false);
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Album album = albumList.get(position);
          holder.txtTenAlbum.setText(album.getTenAlbum());
          holder.txtTenCaSiAlbum.setText(album.getTenCaSiAlbum());
        Glide.with(mContext).load(album.getHinhAlbum()).placeholder(R.drawable.ic_place_holder).into(holder.imgHinhAlbum);
    }

    @Override
    public int getItemCount() {
        return albumList.isEmpty()?0:albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhAlbum;
        TextView txtTenAlbum, txtTenCaSiAlbum;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgHinhAlbum = itemView.findViewById(R.id.img_album);
            txtTenAlbum = itemView.findViewById(R.id.tv_tenAlbum);
            txtTenCaSiAlbum = itemView.findViewById(R.id.tv_tenCaSiALbum);
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
