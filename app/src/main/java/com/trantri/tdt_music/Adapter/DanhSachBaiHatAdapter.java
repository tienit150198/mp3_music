package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.databinding.ItemDanhsachbaihatBinding;

import java.util.List;

public class DanhSachBaiHatAdapter extends RecyclerView.Adapter<DanhSachBaiHatAdapter.ViewHolder> {
    private List<BaiHatYeuThich> mBaiHatYeuThiches;
    private static List<BaiHatYeuThich> mBaiHatDaThiches;
    private AppDatabase mInstanceDatabase;

    public DanhSachBaiHatAdapter(@NonNull Context context, List<BaiHatYeuThich> mBaiHatYeuThiches) {
        mInstanceDatabase = AppDatabase.getInstance(context);
        this.mBaiHatYeuThiches = mBaiHatYeuThiches;
    }

    public void setBaiHatDaThich(List<BaiHatYeuThich> mList) {
        mBaiHatDaThiches = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemDanhsachbaihatBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mBaiHatYeuThiches.get(position), position);
    }

    @Override
    public int getItemCount() {
        return (mBaiHatYeuThiches != null ? mBaiHatYeuThiches.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDanhsachbaihatBinding binding;

        public ViewHolder(ItemDanhsachbaihatBinding b) {
            super(b.getRoot());
            binding = b;
            binding.imgYeuThich.setOnClickListener(v -> {
                if (!mBaiHatYeuThiches.get(getAdapterPosition()).isLiked()) {
                    binding.imgYeuThich.setImageResource(R.drawable.iconloved);
                    mBaiHatYeuThiches.get(getAdapterPosition()).setLiked(true);
                    Toast.makeText(v.getContext(), "Đã Thích", Toast.LENGTH_SHORT).show();

                    mInstanceDatabase.mBaihatBaiHatYeuThichDao().insertBaiHatYeuThich(mBaiHatYeuThiches.get(getAdapterPosition()));
                } else {
                    mBaiHatYeuThiches.get(getAdapterPosition()).setLiked(false);
                    binding.imgYeuThich.setImageResource(R.drawable.iconlove);

                    mInstanceDatabase.mBaihatBaiHatYeuThichDao().deleteBaiHatYeuThich(mBaiHatYeuThiches.get(getAdapterPosition()));
                }
            });
            binding.ClickPlay.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PlayMusicActivity.class);
                intent.putExtra("cakhuc", new Gson().toJson(mBaiHatYeuThiches.get(getAdapterPosition())));
                v.getContext().startActivity(intent);
            });
        }

        public void bindData(BaiHatYeuThich baiHatYeuThich, int position) {
            binding.tvTenCaSiBH.setText(baiHatYeuThich.getTenBaiHat());
            binding.tvTenCaKhuc.setText(baiHatYeuThich.getCaSi());
            binding.tvDanhSachIndex.setText(position + 1 + "");

            binding.imgYeuThich.setImageResource(R.drawable.iconlove);
            if (mBaiHatDaThiches != null) {
                for (int i = 0; i < mBaiHatDaThiches.size(); i++) {
                    if (baiHatYeuThich.getTenBaiHat().equals(mBaiHatDaThiches.get(i).getTenBaiHat())) {
                        binding.imgYeuThich.setImageResource(R.drawable.iconloved);
                        baiHatYeuThich.setLiked(true);
                    }
                }
            }
        }
    }
}
