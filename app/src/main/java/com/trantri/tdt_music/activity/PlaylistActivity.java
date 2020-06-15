package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllPlaylistAdapter;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.databinding.ActivityPlaylistBinding;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaylistActivity extends AppCompatActivity {
    DanhSachAllPlaylistAdapter mAdapter;
    ActivityPlaylistBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        Getdata();
    }

    private static final String TAG = "LOG_PlaylistActivity";

    private void Getdata() {
        Disposable disposable = ApiClient.getService(getApplication()).getAllPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((playlistAlls, throwable) -> {
                    if (throwable != null) {
                        Log.d(TAG, "Getdata: " + throwable.getMessage());
                        return;
                    }
                    mAdapter = new DanhSachAllPlaylistAdapter(PlaylistActivity.this, playlistAlls);
                    binding.myRecycleViewPlaylist.setHasFixedSize(true);
                    binding.myRecycleViewPlaylist.setLayoutManager(new GridLayoutManager(PlaylistActivity.this, 2));
                    binding.myRecycleViewPlaylist.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable);
    }

    private void init() {
        setSupportActionBar(binding.toobarPlaylist);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Playlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toobarPlaylist.setTitleTextColor(getResources().getColor(R.color.BLACK));
        binding.toobarPlaylist.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
