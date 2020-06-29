package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trantri.tdt_music.Adapter.BaiHatAdapter;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.FragmentBaiHatYeuthichBinding;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentBaiHat extends Fragment {
    private BaiHatAdapter mAdapter;
    private FragmentBaiHatYeuthichBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AppDatabase mInstanceDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBaiHatYeuthichBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GetData();
        mInstanceDatabase = AppDatabase.getInstance(getContext());
        mInstanceDatabase.mBaihatBaiHatYeuThichDao().getAllBaiHatYeuThich()
                .observe(Objects.requireNonNull(getActivity()), baiHatYeuThiches -> {
                    if (mAdapter == null) {
                        mAdapter = new BaiHatAdapter(getActivity(), null);
                    }
                    mAdapter.setBaiHatDaThich(baiHatYeuThiches);
                });
    }

    private static final String TAG = "LOG_FragmentBaiHat";

    private void GetData() {

        Disposable disposable = ApiClient.getService(Objects.requireNonNull(getContext())).getDataBaiHatDuocYeuThich()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((baiHatYeuThiches, throwable) -> {
                    if (throwable != null) {
                        Log.d(TAG, "Getdata: " + throwable.getMessage());
                        return;
                    }

                    Log.d(TAG, "GetData: SUCCESS");
                    mAdapter = new BaiHatAdapter(getActivity(), baiHatYeuThiches);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.myRecycleBaiHatYeuThich.setLayoutManager(layoutManager);
                    binding.myRecycleBaiHatYeuThich.setAdapter(mAdapter);
                });
        compositeDisposable.add(disposable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
