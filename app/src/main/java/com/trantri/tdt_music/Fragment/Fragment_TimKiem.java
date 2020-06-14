package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.Adapter.SearchBaiHatAdapter;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.databinding.FragmentTimKiemBinding;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_TimKiem extends Fragment {
    SearchBaiHatAdapter mAdapter;
    FragmentTimKiemBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimKiemBinding.inflate(getLayoutInflater());
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbartimkiembaihat);
        binding.toolbartimkiembaihat.setTitle("Tìm kiếm bài hát...");
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

    public void SearchBaiHat(String keyword) {
        ApiClient.getService(getContext()).getSearchBaiHat(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<BaiHatYeuThich>>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<BaiHatYeuThich> baiHatYeuThiches) {
                        if (baiHatYeuThiches.size() > 0) {
                            Log.d("TAGaaaa", "onNext: "+baiHatYeuThiches.size());
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        DataService dataService = APIService.getService();
//        Call<List<BaiHatYeuThich>> call = dataService.getSearchBaiHat(keyword);
//        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
//            @Override
//            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
//                List<BaiHatYeuThich> listBH = response.body();
//                if (listBH.size() > 0) {
//                    mAdapter = new SearchBaiHatAdapter(getActivity(), listBH);
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    mRecyclerView.setAdapter(mAdapter);
//                    mTextViewKoCoData.setVisibility(View.GONE);
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                } else {
//                    mRecyclerView.setVisibility(View.GONE);
//                    mTextViewKoCoData.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
//
//            }
//        });
    }
}
