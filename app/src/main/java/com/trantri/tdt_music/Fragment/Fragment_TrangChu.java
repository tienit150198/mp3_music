package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.trantri.tdt_music.Adapter.SearchBaiHatAdapter;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.FragmentTrangChuBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_TrangChu extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FragmentTrangChuBinding binding;
    ArrayList<String> list;
    SearchBaiHatAdapter mAdapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrangChuBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchView();

        // config refresh data
        binding.refresh.setOnRefreshListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void searchView() {
        list = new ArrayList<>();
        list.add("sơn tùng");
        list.add("đen vâu");
        list.add("min");

        binding.searchMusic.setSuggestionsEnabled(true);
        binding.searchMusic.setMaxSuggestionCount(5);

        binding.searchMusic.setSpeechMode(false);
        binding.searchMusic.setLastSuggestions(list);
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

        binding.searchMusic.setSpeechMode(false);
        binding.searchMusic.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    binding.recyclerViewSearch.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                binding.searchMusic.closeSearch();
            }
        });
    }

    @Override
    public void onRefresh() {
        binding.refresh.setRefreshing(true);

        new Handler().postDelayed(() -> {
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PAUSE, null));
            Toast.makeText(getContext(), "data refreshed", Toast.LENGTH_SHORT).show();
            binding.refresh.setRefreshing(false);
        }, 1000);
    }
}
