package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachTheLoaiTheoChuDeAdapter;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.data.remote.APIService;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachTheLoaiTheoChuDeBinding;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachTheLoaiTheoChuDeActivity extends AppCompatActivity {

    ChuDe mChuDe;
    DanhSachTheLoaiTheoChuDeAdapter mAdapter;
    ActivityDanhSachTheLoaiTheoChuDeBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhSachTheLoaiTheoChuDeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mChuDe = new ChuDe();
        GetIntent();
        init();
        GetData(mChuDe.getIDChuDe());
    }

    private static final String TAG = "LOG_DanhSachuD";
    private void GetData(String idchude) {
        ApiClient.getService(this).getTheLoaiTheoChuDe(idchude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TheLoai>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<TheLoai> theLoais) {
                        mAdapter = new DanhSachTheLoaiTheoChuDeAdapter(theLoais);
                        binding.myRecycleTheoChuDe.setLayoutManager(new GridLayoutManager(DanhSachTheLoaiTheoChuDeActivity.this, 2));
                        binding.myRecycleTheoChuDe.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
//                        Toast.makeText(DanhSachTheLoaiTheoChuDeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }

    private void init() {
        setSupportActionBar(binding.myToolbarTheoChuDe);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mChuDe.getTenChuDe());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.myToolbarTheoChuDe.setNavigationOnClickListener(v -> finish());
    }


    private void GetIntent() {
        Intent itent = getIntent();

        if (itent != null) {
            if (itent.hasExtra("chude")) {
                mChuDe = (ChuDe) itent.getSerializableExtra("chude");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
