package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.databinding.ItemBaiHatYeuThichBinding;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/28/2020.
 */
public class MusicLikedAdapter extends RecyclerView.Adapter<MusicLikedAdapter.MusicLikedViewHolder> {
    private List<BaiHatYeuThich> mBaiHatYeuThiches;
    private AppDatabase mInstanceDatabase;

    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public MusicLikedAdapter(@NonNull Context context, @NonNull List<BaiHatYeuThich> mBaiHatYeuThiches, @NonNull DanhSachAllChuDeAdapter.OnItemClickedListener listener) {
        this.mBaiHatYeuThiches = mBaiHatYeuThiches;
        mInstanceDatabase = AppDatabase.getInstance(context);

        mListener = listener;
    }

    @NonNull
    @Override
    public MusicLikedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicLikedViewHolder(ItemBaiHatYeuThichBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicLikedViewHolder holder, int position) {
        holder.bindData(mBaiHatYeuThiches.get(position));
    }

    @Override
    public int getItemCount() {
        return (mBaiHatYeuThiches != null ? mBaiHatYeuThiches.size() : 0);
    }

    public class MusicLikedViewHolder extends RecyclerView.ViewHolder {
        private ItemBaiHatYeuThichBinding binding;

        public MusicLikedViewHolder(@NonNull ItemBaiHatYeuThichBinding itemBaiHatYeuThichBinding) {
            super(itemBaiHatYeuThichBinding.getRoot());

            binding = itemBaiHatYeuThichBinding;

            binding.rlBaihat.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));

            binding.imgLuotthich.setOnClickListener(v -> {
                if (!mBaiHatYeuThiches.get(getAdapterPosition()).isLiked()) {
                    binding.imgLuotthich.setImageResource(R.drawable.iconloved);
                    mBaiHatYeuThiches.get(getAdapterPosition()).setLiked(true);
                    Toast.makeText(v.getContext(), "Đã Thích", Toast.LENGTH_SHORT).show();

                    mInstanceDatabase.mBaihatBaiHatYeuThichDao().insertBaiHatYeuThich(mBaiHatYeuThiches.get(getAdapterPosition()));
                } else {
                    mInstanceDatabase.mBaihatBaiHatYeuThichDao().deleteBaiHatYeuThich(mBaiHatYeuThiches.get(getAdapterPosition()));
                    mBaiHatYeuThiches.get(getAdapterPosition()).setLiked(false);
                    binding.imgLuotthich.setImageResource(R.drawable.iconlove);
                }
            });
        }

        public void bindData(BaiHatYeuThich baiHatYeuThich) {
            Glide
                    .with(itemView.getContext())
                    .load(baiHatYeuThich.getHinhBaiHat())
                    .into(binding.imgBaihatyeuthich);

            if (baiHatYeuThich.isLiked()) {
                binding.imgLuotthich.setImageResource(R.drawable.iconloved);
            } else {
                binding.imgLuotthich.setImageResource(R.drawable.iconlove);
            }

            binding.tvTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());
            binding.tvTenCaSi.setText(baiHatYeuThich.getCaSi());
        }
    }
}
