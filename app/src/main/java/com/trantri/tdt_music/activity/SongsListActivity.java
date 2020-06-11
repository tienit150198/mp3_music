package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.trantri.tdt_music.Adapter.DanhSachBaiHatAdapter;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends AppCompatActivity {
    CoordinatorLayout mCoordinatorLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    Button mButtonNgheTatCa;
    ImageView mImageView;
    Quangcao mQuangcao;
    List<BaiHatYeuThich> listBaiHat ;
    DanhSachBaiHatAdapter mAdapter;
    Playlist mPlaylist;
    PlaylistAll mPlaylistAll;
    TheLoai mTheLoai;
    Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        DataItent();
        initView();
        init();

        if (mQuangcao != null && !mQuangcao.getTenbaihat().equals("")) {
            setValuesInView(mQuangcao.getTenbaihat(), mQuangcao.getHinhbaihat());
            getDataQuangCao(mQuangcao.getIdQuangCao());
        }
        if (mPlaylist != null && !mPlaylist.getTen().equals("")) {
            setValuesInView(mPlaylist.getTen(), mPlaylist.getHinhAnhPlaylist());
            getDataPlaylist(mPlaylist.getIdPlaylist());
        }
        if (mPlaylistAll != null && !mPlaylistAll.getTen().equals("")) {
            setValuesInView(mPlaylistAll.getTen(), mPlaylistAll.getHinhNen());
            getDataPlaylist(mPlaylistAll.getIdPlaylist());
        }
        if (mTheLoai != null && !mTheLoai.getTenTheLoai().equals("")) {
            setValuesInView(mTheLoai.getTenTheLoai(), mTheLoai.getHinhTheLoai());
            getDataTheLoai(mTheLoai.getIDTheLoai());
        }
        if (mAlbum != null && !mAlbum.getTenAlbum().equals("")) {
            setValuesInView(mAlbum.getTenAlbum(), mAlbum.getHinhAlbum());
            getDataAlbum(mAlbum.getIdAlbum());
        }
    }

    private void getDataAlbum(String idAlbum) {
        DataService dataService = APIService.getService();
        Call<List<BaiHatYeuThich>> mCall = dataService.getDataBaiHatTheoAlbum(idAlbum);
        mCall.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                listBaiHat = response.body();
                mAdapter = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                mRecyclerView.setAdapter(mAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {

            }
        });
    }

    private void getDataTheLoai(String idtheloai) {
        DataService mDataService = APIService.getService();
        Call<List<BaiHatYeuThich>> mCall = mDataService.getDataBaiHatTheoTheLoai(idtheloai);
        mCall.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                listBaiHat = response.body();
                mAdapter = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                mRecyclerView.setAdapter(mAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {

            }
        });
    }

    private void getDataPlaylist(String idplaylist) {
        DataService mDataService = APIService.getService();
        Call<List<BaiHatYeuThich>> call = mDataService.getDataBaiHatTheoPlaylist(idplaylist);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                listBaiHat = response.body();
                mAdapter = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                mRecyclerView.setAdapter(mAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {

            }
        });
    }

    // lấy data tên bài hát để gắn lên toolbar
    private void setValuesInView(String name, String image) {
        mCollapsingToolbarLayout.setTitle(name);
        try {
            URL mUrl = new URL(image);
            Bitmap mBitmap = BitmapFactory.decodeStream(mUrl.openConnection().getInputStream());
            BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mCollapsingToolbarLayout.setBackground(mBitmapDrawable);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(image).into(mImageView);
    }

    private void getDataQuangCao(String idquangcao) {
        DataService mDataService = APIService.getService();
        Call<List<BaiHatYeuThich>> mCall = mDataService.getDataBaiHatTheoQuangCao(idquangcao);
        mCall.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                listBaiHat = response.body();
                mAdapter = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                mRecyclerView.setAdapter(mAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {

            }
        });
    }


    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
//        mCollapsingToolbarLayout.setExpandedTitleMarginStart(25);
//        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(18);
//        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(18);
        mButtonNgheTatCa.setEnabled(false);
    }

    private void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.myCollapsingToolLayout);
        mCoordinatorLayout = findViewById(R.id.myCooridinerLayout);
        mToolbar = findViewById(R.id.my_toolbarList);
        mRecyclerView = findViewById(R.id.recycleDanhSachBH);
        mButtonNgheTatCa = findViewById(R.id.btn_nghetatca);
        mImageView = findViewById(R.id.img_danhSachbaihat);
    }

    private void DataItent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("quangcao")) {
                mQuangcao = (Quangcao) intent.getSerializableExtra("quangcao");
            }
            if (intent.hasExtra("itemPlaylist")) {
                mPlaylist = (Playlist) intent.getSerializableExtra("itemPlaylist");
            }
            if (intent.hasExtra("itemPlaylistAll")) {
                mPlaylistAll = (PlaylistAll) intent.getSerializableExtra("itemPlaylistAll");
            }
            if (intent.hasExtra("idtheloai")) {
                mTheLoai = (TheLoai) intent.getSerializableExtra("idtheloai");
            }
            if (intent.hasExtra("album")) {
                mAlbum = (Album) intent.getSerializableExtra("album");
            }

        }
    }

    private void eventClick() {
        mButtonNgheTatCa.setEnabled(true);
        mButtonNgheTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongsListActivity.this, PlayMusicActivity.class);
                intent.putParcelableArrayListExtra("allbaihat", (ArrayList<? extends Parcelable>) listBaiHat);
                startActivity(intent);
            }
        });
    }
}
