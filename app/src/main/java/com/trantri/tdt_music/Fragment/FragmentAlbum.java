package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trantri.tdt_music.Adapter.AlbumAdapter;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.activity.DanhSachAllAlbumActivity;
import com.trantri.tdt_music.databinding.FragmentAlbumBinding;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentAlbum extends Fragment {

    private AlbumAdapter mAdapter;
    private FragmentAlbumBinding binding;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAlbumBinding.inflate(getLayoutInflater());
        initview();
        GetDataAlbum();
        return binding.getRoot();
    }

    private void initview() {
        binding.tvNameAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DanhSachAllAlbumActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void GetDataAlbum() {
        disposable.add(
                ApiClient.getService(Objects.requireNonNull(getContext())).getDataAlbum()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((albums) -> {
                            mAdapter = new AlbumAdapter(getActivity(), albums);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            binding.myRecycleAlbum.setLayoutManager(linearLayoutManager);
                            binding.myRecycleAlbum.setAdapter(mAdapter);

                        })

        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }
}
