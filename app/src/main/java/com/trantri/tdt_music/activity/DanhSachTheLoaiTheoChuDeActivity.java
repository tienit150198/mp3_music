package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachTheLoaiTheoChuDeAdapter;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachTheLoaiTheoChuDeBinding;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
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

    private void GetData(String idchude) {
        Disposable disposable = ApiClient.getService(getApplication()).getTheLoaiTheoChuDe(idchude)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(theLoais -> {
                    mAdapter = new DanhSachTheLoaiTheoChuDeAdapter(theLoais);
                    binding.myRecycleTheoChuDe.setLayoutManager(new GridLayoutManager(DanhSachTheLoaiTheoChuDeActivity.this, 2));
                    binding.myRecycleTheoChuDe.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable);
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
