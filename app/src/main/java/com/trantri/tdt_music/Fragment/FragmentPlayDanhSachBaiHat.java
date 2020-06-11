package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.Adapter.PlayMusicAdapter;
import com.trantri.tdt_music.R;

public class FragmentPlayDanhSachBaiHat extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    PlayMusicAdapter musicAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_bai_hat, container, false);
        mRecyclerView = view.findViewById(R.id.recyclePlayDanhSachBH);
        if (PlayMusicActivity.baiHatList.size() > 0 ){
            musicAdapter = new PlayMusicAdapter(getActivity(), PlayMusicActivity.baiHatList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(musicAdapter);
        }
        return view;
    }
}
