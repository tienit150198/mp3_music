package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.trantri.tdt_music.Adapter.YoutubeAdapter;
import com.trantri.tdt_music.Service.ApiMvClient;
import com.trantri.tdt_music.activity.VideoPlayerActivity;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.FragmentMvBinding;
import com.trantri.tdt_music.utils.NetworkUtils;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentMV extends Fragment implements YoutubeAdapter.OnItemClickListener {
    private FragmentMvBinding binding;
    private YoutubeAdapter mYoutubeAdapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMvBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        configData();
        initData();
        searchData();
    }

    private void searchData() {
        binding.searchMv.setSuggestionsEnabled(true);
        binding.searchMv.setMaxSuggestionCount(5);

        binding.searchMv.setSpeechMode(false);

        binding.searchMv.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    binding.searchMv.clearSuggestions();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                compositeDisposable.add(
                        ApiMvClient.getService().queryMvYoutube(String.valueOf(text))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((youtube, throwable) -> {
                                    if (throwable != null) {
                                        if (NetworkUtils.isOnline(Objects.requireNonNull(getContext()))) {
                                            Toast.makeText(getContext(), "Please check you wifi", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Connected fail, please re-connect!", Toast.LENGTH_SHORT).show();
                                        }
                                        return;
                                    }
                                    Toast.makeText(getContext(), "search success!", Toast.LENGTH_SHORT).show();
                                    mYoutubeAdapter.submitList(youtube.getItems());
                                })
                );
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void configData() {
        mYoutubeAdapter = new YoutubeAdapter(this);

        binding.recyclerMv.setAdapter(mYoutubeAdapter);
        binding.recyclerMv.setHasFixedSize(true);
        binding.recyclerMv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static final String TAG = "LOG_FragmentMV";

    private void initData() {
        compositeDisposable.add(
                ApiMvClient.getService().queryMvYoutube("mv")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((youtube, throwable) -> {
                            if (throwable != null) {
                                if (NetworkUtils.isOnline(Objects.requireNonNull(getContext()))) {
                                    Toast.makeText(getContext(), "Please check you wifi", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Connected fail, please re-connect!", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                            mYoutubeAdapter.submitList(youtube.getItems());
                        })
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onItemClicked(String videoId) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra(Constraint.YoutubeVideo.VIDEO_ID, videoId);
        startActivity(intent);
    }
}
