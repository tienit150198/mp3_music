package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemPlayMusicBinding;

import java.util.List;

public class PlayMusicAdapter extends RecyclerView.Adapter<PlayMusicAdapter.ViewHolder> {
    private Context mContext;
    private List<BaiHatYeuThich> listBH;

    public PlayMusicAdapter(Context mContext, List<BaiHatYeuThich> listBH) {
        this.mContext = mContext;
        this.listBH = listBH;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(ItemPlayMusicBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baihat = listBH.get(position);
        holder.binding.tvTenCaSiPlayMusic.setText(baihat.getCaSi());
        holder.binding.tvTenCaSiPlayMusic.setText(baihat.getTenBaiHat());
        holder.binding.tvPlaynhacindex.setText(position + 1 + "");
    }

    @Override
    public int getItemCount() {
        return listBH.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPlayMusicBinding binding;

        public ViewHolder(ItemPlayMusicBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
