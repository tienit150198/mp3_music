package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trantri.tdt_music.Adapter.DanhSachBaiHatAdapter;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.Model.PlaylistAll;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.Model.TheLoai;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.ActivitySongsListBinding;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongsListActivity extends AppCompatActivity {
    private ActivitySongsListBinding binding;
    private Quangcao mQuangcao;
    private List<BaiHatYeuThich> listBaiHat;
    private DanhSachBaiHatAdapter mAdapter;
    private Playlist mPlaylist;
    private PlaylistAll mPlaylistAll;
    private TheLoai mTheLoai;
    private Album mAlbum;

    private AppDatabase mInstanceDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean isUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // SDK Luôn lớn hơn 16 nên sử dụng luôn . không cần check version
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataItent();
        init();

        mInstanceDatabase = AppDatabase.getInstance(this);
        mInstanceDatabase.mBaihatBaiHatYeuThichDao()
                .getAllBaiHatYeuThich()
                .observe(this, baiHatYeuThiches -> {
                    if (mAdapter == null) {
                        mAdapter = new DanhSachBaiHatAdapter(this, null);
                    }
                    mAdapter.setBaiHatDaThich(baiHatYeuThiches);
                });

        if (isUser) {
            mAdapter = new DanhSachBaiHatAdapter(this, listBaiHat);
            binding.recycleDanhSachBH.setAdapter(mAdapter);
        }

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
        Disposable disposable = ApiClient.getService(getApplication()).getDataBaiHatTheoAlbum(idAlbum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baiHatYeuThiches -> {
                    listBaiHat = baiHatYeuThiches;
                    mAdapter = new DanhSachBaiHatAdapter(this, listBaiHat);
                    binding.recycleDanhSachBH.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable);
    }

    private void getDataTheLoai(String idtheloai) {
        Disposable disposable1 = ApiClient.getService(getApplicationContext()).getDataBaiHatTheoTheLoai(idtheloai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baiHatYeuThiches -> {
                    listBaiHat = baiHatYeuThiches;
                    mAdapter = new DanhSachBaiHatAdapter(this, listBaiHat);
                    binding.recycleDanhSachBH.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable1);
    }

    private void getDataPlaylist(String idplaylist) {
        Disposable disposable2 = ApiClient.getService(getApplicationContext()).getDataBaiHatTheoPlaylist(idplaylist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baiHatYeuThiches -> {
                    listBaiHat = baiHatYeuThiches;
                    mAdapter = new DanhSachBaiHatAdapter(this, listBaiHat);
                    binding.recycleDanhSachBH.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable2);
    }

    // lấy data tên bài hát để gắn lên toolbar
    private void setValuesInView(String name, String image) {
        binding.myCollapsingToolLayout.setTitle(name);
        try {
            URL mUrl = new URL(image);
            Bitmap mBitmap = BitmapFactory.decodeStream(mUrl.openConnection().getInputStream());
            BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                binding.myCollapsingToolLayout.setBackground(mBitmapDrawable);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(image).into(binding.imgDanhSachbaihat);
    }

    private void getDataQuangCao(String idquangcao) {
        Disposable disposable3 = ApiClient.getService(getApplicationContext()).getDataBaiHatTheoQuangCao(idquangcao)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baiHatYeuThiches -> {
                    listBaiHat = baiHatYeuThiches;
                    mAdapter = new DanhSachBaiHatAdapter(this, listBaiHat);
                    binding.recycleDanhSachBH.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable3);
    }


    private void init() {
        setSupportActionBar(binding.myToolbarList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.myToolbarList.setNavigationOnClickListener(v -> finish());
        binding.btnNghetatca.setEnabled(false);

        binding.recycleDanhSachBH.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
        eventClick();
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
            if (intent.hasExtra(Constraint.Intent.USER)) {
                String txtAllBaiHat = intent.getStringExtra(Constraint.Intent.USER);
                Type type = new TypeToken<List<BaiHatYeuThich>>() {
                }.getType();
                listBaiHat = new Gson().fromJson(txtAllBaiHat, type);

                isUser = true;

                Glide
                        .with(this)
                        .load(listBaiHat.get(0).getHinhBaiHat())
                        .into(binding.imgDanhSachbaihat);

                try {
                    URL url = new URL(listBaiHat.get(0).getHinhBaiHat());
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Drawable image = new BitmapDrawable(getResources(), bitmap);

                    binding.myCollapsingToolLayout.setBackground(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private static final String TAG = "xxxx SONGLISTACTIVITY";

    private void eventClick() {
        binding.btnNghetatca.setEnabled(true);
        binding.btnNghetatca.setOnClickListener(v -> {
            Intent intent = new Intent(SongsListActivity.this, PlayMusicActivity.class);
            intent.putExtra("allbaihat", new Gson().toJson(listBaiHat));
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
