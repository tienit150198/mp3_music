package com.trantri.tdt_music.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.databinding.ItemDanhsachbaihatBinding;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachBaiHatAdapter extends RecyclerView.Adapter<DanhSachBaiHatAdapter.ViewHolder> {

    List<BaiHatYeuThich> list;


    public DanhSachBaiHatAdapter(List<BaiHatYeuThich> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemDanhsachbaihatBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = list.get(position);
        holder.binding.tvTenCaSiBH.setText(baiHatYeuThich.getCaSi());
        holder.binding.tvTenCaKhuc.setText(baiHatYeuThich.getCaSi());
        holder.binding.tvDanhSachIndex.setText(position + 1 + "");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDanhsachbaihatBinding binding;

        public ViewHolder(ItemDanhsachbaihatBinding b) {
            super(b.getRoot());
            binding = b;
            binding.imgYeuThich.setOnClickListener(v -> {
                binding.imgYeuThich.setImageResource(R.drawable.iconloved);
                ApiClient.getService(v.getContext()).getDataLuotLikeBaiHat("1", list.get(getAdapterPosition()).getIdBaiHat())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                                if (s.equals("OK")) {
                                    Toast.makeText(v.getContext(), "Đã Thích Cám Ơn", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Please Check Again !", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            });
            binding.ClickPlay.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PlayMusicActivity.class);
                intent.putExtra("cakhuc", list.get(getAdapterPosition()));
                v.getContext().startActivity(intent);
            });
        }
    }
}
