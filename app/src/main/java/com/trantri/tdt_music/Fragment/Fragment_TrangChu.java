package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.trantri.tdt_music.Adapter.SearchBaiHatAdapter;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.databinding.FragmentTrangChuBinding;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_TrangChu extends Fragment {
    FragmentTrangChuBinding binding;
    ArrayList<String> list;
    SearchBaiHatAdapter mAdapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrangChuBinding.inflate(getLayoutInflater());
        searchView();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void searchView() {


        list = new ArrayList<>();
        list.add("nhạc trẻ");
        list.add("nhạc vàng");
        list.add("nhạc cách mạng");
        binding.searchMusic.setSuggestionsEnabled(true);

        binding.searchMusic.setLastSuggestions(list);
        binding.searchMusic.setCardViewElevation(10);
        binding.searchMusic.setClearIcon(R.drawable.ic_baseline_delete_24);
        binding.searchMusic.setTextColor(R.color.white);

        binding.searchMusic.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    binding.searchMusic.clearSuggestions();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                SearchBaiHat(String.valueOf(text));
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {


                }
            }
        });
    }

    private void SearchBaiHat(String keyword) {
        compositeDisposable.add(
                ApiClient.getService(Objects.requireNonNull(getContext())).getSearchBaiHat(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(baiHatYeuThiches -> {
                            if (baiHatYeuThiches.size() > 0) {
                                Log.d("TAGaaaa", "onNext: " + baiHatYeuThiches.size());
                                mAdapter = new SearchBaiHatAdapter(getActivity(), baiHatYeuThiches);
                                binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                                binding.recyclerViewSearch.setAdapter(mAdapter);
                                binding.recyclerViewSearch.setVisibility(View.GONE);
                                binding.recyclerViewSearch.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                binding.recyclerViewSearch.setVisibility(View.GONE);
//                            binding.recyclerViewSearch.setVisibility(View.VISIBLE);
//                            mAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Không có bài hát tương ứng!", Toast.LENGTH_SHORT).show();

                            }
                        })
        );

        binding.searchMusic.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0) {
//                    mAdapter.notifyDataSetChanged();

                    binding.recyclerViewSearch.setVisibility(View.GONE);

                }
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
//                binding.searchMusic.closeSearch();
            }
        });
    }
}
