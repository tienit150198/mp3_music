package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.activity.DanhSachAllChuDeActivity;
import com.trantri.tdt_music.activity.DanhSachTheLoaiTheoChuDeActivity;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Model.ChuDeAndTheLoai;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChuDeThLoai extends Fragment {
    View view;
    HorizontalScrollView mHorizontalScrollView;
    TextView txtXemThem;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chude_theloai, container, false);
        initView();
        GetData();
        return view;
    }

    private void initView() {
        mHorizontalScrollView = view.findViewById(R.id.myScollChudeTheLoai);
        txtXemThem = view.findViewById(R.id.tv_xemthem);
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachAllChuDeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        DataService mDataService = APIService.getService();

        disposable.add(
                mDataService.getDataChuDeTheLoai()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((chuDeAndTheLoai, throwable) -> {
                    if(throwable != null){
                        // error
                    }else{
                        // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng chủ đề
                        final ArrayList<ChuDe> chuDeArrayList = new ArrayList<>(chuDeAndTheLoai != null ? chuDeAndTheLoai.getChuDe() : null);
                        // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng thể loại
                        final ArrayList<TheLoai> theLoaiArrayList = new ArrayList<TheLoai>(chuDeAndTheLoai.getTheLoai());

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
                            mImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), DanhSachTheLoaiTheoChuDeActivity.class);
                                    intent.putExtra("chude", chuDeArrayList.get(finalI));
//                            Log.d("BBB", chuDeArrayList.get(0).getIDChuDe());
                                    startActivity(intent);
                                }
                            });
                        }
                        for (int j = 0; j < theLoaiArrayList.size(); j++) {
                            CardView mCardView = new CardView(Objects.requireNonNull(getActivity()));
                            mCardView.setRadius(10); // bo xung quanh 10dp
                            ImageView mImageView = new ImageView(getActivity());
                            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            if (theLoaiArrayList.get(j).getHinhTheLoai() != null) {
                                Glide.with(getActivity()).load(theLoaiArrayList.get(j).getHinhTheLoai()).into(mImageView);
                            }
                            mCardView.setLayoutParams(mLayoutParams);
                            mCardView.addView(mImageView);
                            mLinearLayout.addView(mCardView);

                            final int finalJ = j;
                            mImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), SongsListActivity.class);
                                    intent.putExtra("idtheloai", theLoaiArrayList.get(finalJ));
                                    startActivity(intent);
                                }
                            });
                        }
                        mHorizontalScrollView.addView(mLinearLayout);
                    }
                })

        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
