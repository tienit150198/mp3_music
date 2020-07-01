package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.activity.DanhSachAllChuDeActivity;
import com.trantri.tdt_music.activity.DanhSachTheLoaiTheoChuDeActivity;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.databinding.FragmentChudeTheloaiBinding;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentChuDeThLoai extends Fragment {
    private FragmentChudeTheloaiBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChudeTheloaiBinding.inflate(inflater, container, false);
        initView();
        GetData();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        binding.tvChudetheloai.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DanhSachAllChuDeActivity.class);
            startActivity(intent);
        });
    }

    private static final String TAG = "LOG_FragmentChuDeThLoai";

    private void GetData() {
        Disposable disposable = ApiClient.getService(Objects.requireNonNull(getContext())).getDataChuDeTheLoai()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((chuDeAndTheLoai, throwable) -> {
                    if (throwable != null) {
                        Log.d(TAG, "Getdata: " + throwable.getMessage());
                        return;
                    }
                    // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng chủ đề
                    final ArrayList<ChuDe> chuDeArrayList = new ArrayList<>(chuDeAndTheLoai.getChuDe());
                    // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng thể loại
                    final ArrayList<TheLoai> theLoaiArrayList = new ArrayList<>(chuDeAndTheLoai.getTheLoai());

                    LinearLayout mLinearLayout = new LinearLayout(getActivity());
                    mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    // set lại kích thước cho layout
                    LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(580, 250);
                    mLayoutParams.setMargins(10, 20, 10, 30);

                    for (int i = 0; i < chuDeArrayList.size(); i++) {
                        CardView mCardView = new CardView(Objects.requireNonNull(getActivity()));
                        mCardView.setRadius(10); // bo xung quanh 10dp
                        ImageView mImageView = new ImageView(getActivity());
                        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (chuDeArrayList.get(i).getHinhChuDe() != null) {
                            Glide.with(getActivity()).load(chuDeArrayList.get(i).getHinhChuDe()).into(mImageView);
                        }
                        mCardView.setLayoutParams(mLayoutParams);
                        mCardView.addView(mImageView);
                        mLinearLayout.addView(mCardView);
                        final int finalI = i;
                        mImageView.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), DanhSachTheLoaiTheoChuDeActivity.class);
                            intent.putExtra("chude", chuDeArrayList.get(finalI));
//                            Log.d("BBB", chuDeArrayList.get(0).getIDChuDe());
                            startActivity(intent);
                        });
                    }
                    for (int j = 0; j < theLoaiArrayList.size(); j++) {
                        CardView mCardView = new CardView(Objects.requireNonNull(getActivity()));
                        mCardView.setRadius(15); // bo xung quanh 10dp
                        ImageView mImageView = new ImageView(getActivity());
                        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (theLoaiArrayList.get(j).getHinhTheLoai() != null) {
                            Glide.with(getActivity()).load(theLoaiArrayList.get(j).getHinhTheLoai())
                                    .transform(new CenterInside(), new RoundedCorners(30))
                                    .into(mImageView);
                        }
                        mCardView.setLayoutParams(mLayoutParams);
                        mCardView.addView(mImageView);
                        mLinearLayout.addView(mCardView);

                        final int finalJ = j;
                        mImageView.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), SongsListActivity.class);
                            intent.putExtra("idtheloai", theLoaiArrayList.get(finalJ));
                            startActivity(intent);
                        });
                    }

                    if (!binding.myScollChudeTheLoai.isFillViewport()) {
                        binding.myScollChudeTheLoai.addView(mLinearLayout);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
