package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Adapter.DanhSachAllPlaylistAdapter;
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.databinding.ActivityPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void Getdata() {
        Disposable disposable = ApiClient.getService(getApplication()).getAllPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<PlaylistAll>>() {
                    @Override
                    public void onNext(@NonNull List<PlaylistAll> playlistAlls) {
                        mAdapter = new DanhSachAllPlaylistAdapter(PlaylistActivity.this, playlistAlls);
                        binding.myRecycleViewPlaylist.setHasFixedSize(true);
                        binding.myRecycleViewPlaylist.setLayoutManager(new GridLayoutManager(PlaylistActivity.this, 2));
                        binding.myRecycleViewPlaylist.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(PlaylistActivity.this, "Dữ liệu lỗi ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void init() {
        setSupportActionBar(binding.toobarPlaylist);
        getSupportActionBar().setTitle("Playlist");
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
