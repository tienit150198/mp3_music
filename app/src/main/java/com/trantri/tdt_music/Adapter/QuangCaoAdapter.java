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
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.R;

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
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_quangcao, null);

        ImageView imgBackgroundQC = view.findViewById(R.id.img_backgroundQC);
        ImageView imgQC = view.findViewById(R.id.img_QC);
        TextView txtTitleBanner = view.findViewById(R.id.tv_titleBanner);
        TextView txtNoiDungQC = view.findViewById(R.id.tv_mieutaBH);

        Glide.with(mContext).load(mListQC.get(position).getHinhanh()).placeholder(R.drawable.ic_place_holder).into(imgBackgroundQC);
        Glide.with(mContext).load(mListQC.get(position).getHinhbaihat()).placeholder(R.drawable.ic_place_holder).into(imgQC);
        txtTitleBanner.setText(mListQC.get(position).getTenbaihat());
        txtNoiDungQC.setText(mListQC.get(position).getNoidung());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SongsListActivity.class);
                intent.putExtra("quangcao", mListQC.get(position));
                mContext.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // sau khi thuc hien xong thì xóa view
        container.removeView((View) object);
    }
}
