package com.trantri.tdt_music.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Adapter.BaiHatAdapter;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.databinding.FragmentBaiHatYeuthichBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentBaiHat extends Fragment {
    BaiHatAdapter mAdapter;
    FragmentBaiHatYeuthichBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBaiHatYeuthichBinding.inflate(getLayoutInflater());
        GetData();
        return binding.getRoot();
    }

    private void GetData() {

        Disposable disposable = ApiClient.getService(getContext()).getDataBaiHatDuocYeuThich()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<BaiHatYeuThich>>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<BaiHatYeuThich> baiHatYeuThiches) {
                        mAdapter = new BaiHatAdapter(getActivity(), baiHatYeuThiches);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.myRecycleBaiHatYeuThich.setLayoutManager(layoutManager);
                        binding.myRecycleBaiHatYeuThich.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
