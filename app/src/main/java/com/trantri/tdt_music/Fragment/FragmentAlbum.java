package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Adapter.AlbumAdapter;
import com.trantri.tdt_music.Model.Album;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.activity.DanhSachAllAlbumActivity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAlbum extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    TextView txtXemThemAlbum;
    AlbumAdapter mAdapter;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        initview();
        GetDataAlbum();
        return view;
    }

    private void initview() {
        mRecyclerView = view.findViewById(R.id.myRecycleAlbum);
        txtXemThemAlbum = view.findViewById(R.id.tv_xemthemAlbum);
        txtXemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachAllAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void GetDataAlbum() {
        DataService mDataService = APIService.getService();

        disposable.add(
                mDataService.getDataAlbum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((albums, throwable) -> {
                    if(throwable != null){
                        // error
                    }else{
                        mAdapter = new AlbumAdapter(getActivity(), albums);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                })

        );

    }
}
