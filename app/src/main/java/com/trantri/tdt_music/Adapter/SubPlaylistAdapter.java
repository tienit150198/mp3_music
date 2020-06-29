package com.trantri.tdt_music.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.databinding.ItemAlbumBinding;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
public class SubPlaylistAdapter extends RecyclerView.Adapter<SubPlaylistAdapter.SubPlayListViewHolder> {
    private List<BaiHatYeuThich> mBaiHatYeuThiches;

    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public SubPlaylistAdapter(@NonNull List<BaiHatYeuThich> mBaiHatYeuThiches, @NonNull DanhSachAllChuDeAdapter.OnItemClickedListener listener) {
        this.mBaiHatYeuThiches = mBaiHatYeuThiches;
        mListener = listener;
    }

    @NonNull
    @Override
    public SubPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubPlayListViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlayListViewHolder holder, int position) {
        holder.bindData(mBaiHatYeuThiches.get(position));
    }

    private static final String TAG = "121212";

    @Override
    public int getItemCount() {
        return (mBaiHatYeuThiches != null ? mBaiHatYeuThiches.size() : 0);
    }

    public class SubPlayListViewHolder extends RecyclerView.ViewHolder {
        private ItemAlbumBinding binding;

        public SubPlayListViewHolder(@NonNull ItemAlbumBinding itemAlbumBinding) {
            super(itemAlbumBinding.getRoot());
            binding = itemAlbumBinding;

            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(getAdapterPosition());
                Log.d(TAG, "SubPlayListViewHolder: " + getAdapterPosition());
            });
        }

        public void bindData(BaiHatYeuThich baiHatYeuThich) {
            Glide
                    .with(itemView.getContext())
                    .load(baiHatYeuThich.getHinhBaiHat())
                    .into(binding.imgAlbum);

            binding.tvTenAlbum.setText(baiHatYeuThich.getTenBaiHat());
            binding.tvTenCaSiALbum.setText(baiHatYeuThich.getCaSi());
        }
    }
}
