package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllChuDeAdapter;
import com.trantri.tdt_music.Model.ChuDe;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachAllChuDeBinding;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachAllChuDeActivity extends AppCompatActivity {
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
        Disposable disposable = ApiClient.getService(getApplication()).getAllChuDe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((chuDes, throwable) -> {
                    if(throwable != null){
                        Log.d(TAG, "GetDataChuDe: err");
                        return;
                    }
                    mAdapter = new DanhSachAllChuDeAdapter(getApplicationContext(), chuDes);
                    binding.recycleViewAllChuDe.setLayoutManager(new GridLayoutManager(DanhSachAllChuDeActivity.this, 1));
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
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
