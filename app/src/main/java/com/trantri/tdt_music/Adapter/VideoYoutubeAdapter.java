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
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_youtube, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        YoutubeMusic music = musicList.get(position);
        holder.txtTitle.setText(music.getmTitle());
        Glide.with(context).load(music.getmThumbnail()).placeholder(R.drawable.ic_place_holder).into(holder.imageViewThumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnClickListener(v,   holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tv_titleVideo);
            imageViewThumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }

    public interface OnClickItemYouTube {
        void setOnClickListener(View view, int position);
    }
}
