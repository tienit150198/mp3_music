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
import com.trantri.tdt_music.databinding.FragmentPlayDanhSachBaiHatBinding;

public class FragmentPlayDanhSachBaiHat extends Fragment {
    PlayMusicAdapter musicAdapter;
    FragmentPlayDanhSachBaiHatBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding =  FragmentPlayDanhSachBaiHatBinding.inflate(getLayoutInflater());

        if (PlayMusicActivity.baiHatList.size() > 0 ){
            musicAdapter = new PlayMusicAdapter(getActivity(), PlayMusicActivity.baiHatList);
            binding.recyclePlayDanhSachBH.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclePlayDanhSachBH.setAdapter(musicAdapter);
        }
        return binding.getRoot();
    }
}
