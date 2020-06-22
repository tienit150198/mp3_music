package com.trantri.tdt_music.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllAlbumAdapter;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.ActivityDanhSachAllAlbumBinding;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
