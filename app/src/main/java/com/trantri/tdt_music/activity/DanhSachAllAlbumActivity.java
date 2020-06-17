package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllAlbumAdapter;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachAllAlbumBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachAllAlbumActivity extends AppCompatActivity {
    DanhSachAllAlbumAdapter mDanhSachAllAlbumAdapter;
    ActivityDanhSachAllAlbumBinding binding;
    private  CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhSachAllAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        GetData();
    }

    private void initView() {
        setSupportActionBar(binding.toolbarAllAlbum);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tất Cả AlBum Bài Hát");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarAllAlbum.setNavigationOnClickListener(v -> finish());
    }

    private void GetData() {
        Disposable disposable = ApiClient.getService(getApplicationContext()).getAllAlbum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albums -> {
                    mDanhSachAllAlbumAdapter = new DanhSachAllAlbumAdapter(albums);
                    binding.recycleAllAlbum.setLayoutManager(new GridLayoutManager(DanhSachAllAlbumActivity.this, 2));
                    binding.recycleAllAlbum.setAdapter(mDanhSachAllAlbumAdapter);
                });
        compositeDisposable.add(disposable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
