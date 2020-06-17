package com.trantri.tdt_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.databinding.ItemBaiHatYeuThichBinding;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> baiHatYeuThichList;

    public BaiHatAdapter(Context mContext, List<BaiHatYeuThich> baiHatYeuThichList) {
        this.mContext = mContext;
        this.baiHatYeuThichList = baiHatYeuThichList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(ItemBaiHatYeuThichBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = baiHatYeuThichList.get(position);
        holder.binding.tvTenCaSi.setText(baiHatYeuThich.getCaSi());
        holder.binding.tvTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());

        Glide.with(mContext).load(baiHatYeuThich.getHinhBaiHat())
                .transform(new CenterInside(), new RoundedCorners(15))
                .placeholder(R.drawable.ic_place_holder)
                .into(holder.binding.imgBaihatyeuthich);

    }

    @Override
    public int getItemCount() {
        return baiHatYeuThichList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBaiHatYeuThichBinding binding;
        public ViewHolder(ItemBaiHatYeuThichBinding b) {
            super(b.getRoot());
            binding = b;
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("cakhuc", baiHatYeuThichList.get(getLayoutPosition()));
                mContext.startActivity(intent);
            });
            binding.imgLuotthich.setOnClickListener(v->{
                binding.imgLuotthich.setImageResource(R.drawable.iconloved);
                ApiClient.getService(v.getContext()).getDataLuotLikeBaiHat("1",baiHatYeuThichList.get(getLayoutPosition()).getIdBaiHat())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                                if(s.equals("OK")){
                                    Toast.makeText(mContext,"Đã Thích Cám Ơn",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mContext,"Please Check Again !",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                binding.imgLuotthich.setEnabled(false);
            });
        }
    }
}
