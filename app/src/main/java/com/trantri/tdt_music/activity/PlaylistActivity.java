package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Adapter.DanhSachAllPlaylistAdapter;
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    DanhSachAllPlaylistAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initView();
        init();
        Getdata();
    }

    private void Getdata() {
        DataService mDataService = APIService.getService();
        Call<List<PlaylistAll>> callBack = mDataService.getAllPlaylist();
        callBack.enqueue(new Callback<List<PlaylistAll>>() {
            @Override
            public void onResponse(Call<List<PlaylistAll>> call, Response<List<PlaylistAll>> response) {
                ArrayList<PlaylistAll> playlists = (ArrayList<PlaylistAll>) response.body();
                mAdapter = new DanhSachAllPlaylistAdapter(PlaylistActivity.this, playlists);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new GridLayoutManager(PlaylistActivity.this, 2));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<PlaylistAll>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Playlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.BLACK));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.myRecycleViewPlaylist);
        mToolbar = findViewById(R.id.toobarPlaylist);
    }
}
