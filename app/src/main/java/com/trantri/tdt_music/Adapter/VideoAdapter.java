package com.trantri.tdt_music.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Model.MyVideo;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemMyVideoBinding;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    List<MyVideo> youtubeVideoList;

    public VideoAdapter(List<MyVideo> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VideoViewHolder(ItemMyVideoBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.b.myWebViewVideoView.loadData( youtubeVideoList.get(position).getmVideoUrl(), "text/html" , "utf-8" );
    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        ItemMyVideoBinding b;
     public VideoViewHolder(ItemMyVideoBinding itemView) {
         super(itemView.getRoot());
         b = itemView;

         b.myWebViewVideoView.getSettings().setJavaScriptEnabled(true);
         b.myWebViewVideoView.setWebChromeClient(new WebChromeClient() {


         } );
     }
 }
}
