package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.databinding.ItemBaiHatYeuThichBinding;

import java.util.List;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.ViewHolder> {
    private Context mContext;
    private List<BaiHatYeuThich> mBaiHatYeuThiches;
    private static List<BaiHatYeuThich> mBatHatDaThiches;
    private AppDatabase mInstanceDatabase;

    public BaiHatAdapter(Context mContext, List<BaiHatYeuThich> mBaiHatYeuThiches) {
        this.mContext = mContext;
        this.mBaiHatYeuThiches = mBaiHatYeuThiches;
        mInstanceDatabase = AppDatabase.getInstance(mContext);
    }

    public void setBaiHatDaThich(List<BaiHatYeuThich> mList) {
        mBatHatDaThiches = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(ItemBaiHatYeuThichBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mBaiHatYeuThiches.get(position));
    }

    @Override
    public int getItemCount() {
        return (mBaiHatYeuThiches != null ? mBaiHatYeuThiches.size() : 0);
    }

    private static final String TAG = "xxxxxx";
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBaiHatYeuThichBinding binding;

        public ViewHolder(ItemBaiHatYeuThichBinding b) {
            super(b.getRoot());
            binding = b;
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("cakhuc", new Gson().toJson(mBaiHatYeuThiches.get(getLayoutPosition())));
                mContext.startActivity(intent);
            });

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
                    .transform(new CenterInside(), new RoundedCorners(15))
                    .into(binding.imgBaihatyeuthich);

            binding.imgLuotthich.setImageResource(R.drawable.iconlove);
            if (mBatHatDaThiches != null) {
                for (int i = 0; i < mBatHatDaThiches.size(); i++) {
                    if (baiHatYeuThich.getTenBaiHat().equals(mBatHatDaThiches.get(i).getTenBaiHat())) {
                        binding.imgLuotthich.setImageResource(R.drawable.iconloved);
                        baiHatYeuThich.setLiked(true);
                    }
                }
            }

            binding.tvTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());
            binding.tvTenCaSi.setText(baiHatYeuThich.getCaSi());
        }
    }
}
