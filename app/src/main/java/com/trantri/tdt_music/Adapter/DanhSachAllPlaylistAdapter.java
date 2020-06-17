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
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemAllplaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class DanhSachAllPlaylistAdapter extends RecyclerView.Adapter<DanhSachAllPlaylistAdapter.ViewHolder> {
    Context mContext;
    List<PlaylistAll> list;

    public DanhSachAllPlaylistAdapter(Context mContext, List<PlaylistAll> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(ItemAllplaylistBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistAll playlist = list.get(position);
        // ??? getHinhAnhPlaylist = null
        Glide.with(mContext)
                .load(list.get(position).getHinhNen())
//                .placeholder(R.drawable.ic_place_holder)
//                .error(R.drawable.ic_place_holder)
                .into(holder.binding.imgDanhsachallBH);
        holder.binding.tvTenCaSiPlaylist.setText(playlist.getTen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAllplaylistBinding binding;

        public ViewHolder(@NonNull ItemAllplaylistBinding b) {
            super(b.getRoot());
            binding = b;
            binding.lnList.setOnClickListener(v->{
                Intent intent = new Intent(mContext, SongsListActivity.class);
                intent.putExtra("itemPlaylistAll", list.get(getPosition()));
                mContext.startActivity(intent);
            });
        }
    }
}
