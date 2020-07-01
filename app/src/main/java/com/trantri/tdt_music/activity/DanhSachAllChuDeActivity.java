package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllChuDeAdapter;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachAllChuDeBinding;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachAllChuDeActivity extends AppCompatActivity implements DanhSachAllChuDeAdapter.OnItemClickedListener {
    DanhSachAllChuDeAdapter mAdapter;
    ActivityDanhSachAllChuDeBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhSachAllChuDeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar();
        GetDataChuDe();
    }

    private static final String TAG = "LOG_DSAllChuDe";

    private void GetDataChuDe() {
        binding.recycleViewAllChuDe.setLayoutManager(new GridLayoutManager(DanhSachAllChuDeActivity.this, 1));
        Disposable disposable = ApiClient.getService(getApplication()).getAllChuDe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((chuDes, throwable) -> {
                    if (throwable != null) {
                        Log.d(TAG, "GetDataChuDe: err");
                        return;
                    }
                    mAdapter = new DanhSachAllChuDeAdapter(chuDes, this);
                    binding.recycleViewAllChuDe.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable);

    }

    private void initToolbar() {
        setSupportActionBar(binding.toobarAllChuDe);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề Bài Hát");
        binding.toobarAllChuDe.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onItemClicked(@NonNull ChuDe chude) {
        Intent intent = new Intent(this, DanhSachTheLoaiTheoChuDeActivity.class);
        intent.putExtra("chude", chude);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
