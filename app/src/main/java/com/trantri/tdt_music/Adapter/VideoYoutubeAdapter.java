package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.Model.YoutubeMusic;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemVideoYoutubeBinding;

import java.util.List;

public class VideoYoutubeAdapter extends RecyclerView.Adapter<VideoYoutubeAdapter.ViewHolder> {
    private Context context;
    private List<YoutubeMusic> musicList;
    private OnClickItemYouTube mListener;

    public VideoYoutubeAdapter(Context context, List<YoutubeMusic> musicList, OnClickItemYouTube mListener) {
        this.context = context;
        this.musicList = musicList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(ItemVideoYoutubeBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        YoutubeMusic music = musicList.get(position);
        holder.binding.tvTitleVideo.setText(music.getmTitle());
        Glide.with(context)
                .load(music.getmThumbnail())
//                .placeholder(R.drawable.ic_place_holder)
                .into(holder.binding.imgThumbnail);
        holder.itemView.setOnClickListener(v -> mListener.setOnClickListener(v, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemVideoYoutubeBinding binding;

        public ViewHolder(ItemVideoYoutubeBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }

    public interface OnClickItemYouTube {
        void setOnClickListener(View view, int position);
    }
}
