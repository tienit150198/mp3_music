package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ItemQuangcaoBinding;

import java.util.ArrayList;

public class QuangCaoAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Quangcao> mListQC;

    public QuangCaoAdapter(Context mContext, ArrayList<Quangcao> mListQC) {
        this.mContext = mContext;
        this.mListQC = mListQC;
    }

    @Override
    public int getCount() {
        return !mListQC.isEmpty()?mListQC.size():0 ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    // định hình object và gán dữ liệu cho mỗi object tượng trưng cho mỗi cái page
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(30));

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemQuangcaoBinding binding = ItemQuangcaoBinding.inflate(inflater);

        Glide.with(mContext).load(mListQC.get(position)
                .getHinhanh())
                .transform(new CenterInside(), new RoundedCorners(30))
//                .placeholder(R.drawable.ic_place_holder)
                .into(binding.imgBackgroundQC);
        Glide.with(mContext).load(mListQC.get(position)
                .getHinhbaihat())
                .transform(new CenterInside(), new RoundedCorners(15))
//                .placeholder(R.drawable.ic_place_holder)
                .into(binding.imgQC);
        binding.tvTitleBanner.setText(mListQC.get(position).getTenbaihat());
        binding.tvMieutaBH.setText(mListQC.get(position).getNoidung());
        binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongsListActivity.class);
            intent.putExtra("quangcao", mListQC.get(position));
            mContext.startActivity(intent);
        });
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // sau khi thuc hien xong thì xóa view
        container.removeView((View) object);
    }
}
