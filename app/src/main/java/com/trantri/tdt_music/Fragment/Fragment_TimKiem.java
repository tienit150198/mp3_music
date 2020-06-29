package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trantri.tdt_music.Adapter.SearchBaiHatAdapter;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.FragmentTimKiemBinding;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_TimKiem extends Fragment {
    private SearchBaiHatAdapter mAdapter;
    private FragmentTimKiemBinding binding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimKiemBinding.inflate(inflater, container, false);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(binding.toolbartimkiembaihat);
        binding.toolbartimkiembaihat.setTitle("Tìm kiếm bài hát...");
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.mySearchBH);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchBaiHat(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private static final String TAG = "LOG_Fragment_TimKiem";

    private void SearchBaiHat(String keyword) {
        ApiClient.getService(Objects.requireNonNull(getContext())).getSearchBaiHat(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BaiHatYeuThich>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<BaiHatYeuThich> baiHatYeuThiches) {
                        if (baiHatYeuThiches.size() > 0) {
                            Log.d("TAGaaaa", "onNext: " + baiHatYeuThiches.size());
                            mAdapter = new SearchBaiHatAdapter(getActivity(), baiHatYeuThiches);
                            binding.recycleviewTimKiem.setLayoutManager(new LinearLayoutManager(getActivity()));
                            binding.recycleviewTimKiem.setAdapter(mAdapter);
                            binding.recycleviewTimKiem.setVisibility(View.GONE);
                            binding.recycleviewTimKiem.setVisibility(View.VISIBLE);
                        } else {
                            binding.recycleviewTimKiem.setVisibility(View.GONE);
                            binding.tvDataNull.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
