package com.trantri.tdt_music.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Adapter.DanhSachAllChuDeAdapter;
import com.trantri.tdt_music.Model.Banner;
import com.trantri.tdt_music.Model.Category;

import java.util.List;

/**
 * Created by TranTien
 * Date 07/01/2020.
 */
public class PlaylistUserAdapter extends RecyclerView.Adapter<PlaylistUserAdapter.ViewHolder> {
    private List<Banner> mBanners;
    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public PlaylistUserAdapter(List<Banner> mBanners,@NonNull DanhSachAllChuDeAdapter.OnItemClickedListener mListener) {
        this.mBanners = mBanners;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(com.trantri.tdt_music.databinding.ItemTheLoaiTheoChuDeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mBanners.get(position));
    }

    @Override
    public int getItemCount() {
        return (mBanners != null ? mBanners.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private com.trantri.tdt_music.databinding.ItemTheLoaiTheoChuDeBinding binding;
        public ViewHolder(@NonNull com.trantri.tdt_music.databinding.ItemTheLoaiTheoChuDeBinding itemTheLoaiTheoChuDeBinding) {
            super(itemTheLoaiTheoChuDeBinding.getRoot());

            binding = itemTheLoaiTheoChuDeBinding;
            itemView.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
        }

        public void bindData(Banner banner){
            Glide
                    .with(itemView.getContext())
                    .load(banner.getImage())
                    .into(binding.imgTheloaitheochude);

            binding.tvTheloaitheochude.setText(banner.getName());
        }
    }
}
