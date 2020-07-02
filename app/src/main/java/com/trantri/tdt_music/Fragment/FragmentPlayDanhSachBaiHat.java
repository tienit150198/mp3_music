package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trantri.tdt_music.Adapter.DanhSachAllChuDeAdapter;
import com.trantri.tdt_music.Adapter.PlayMusicAdapter;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.FragmentPlayDanhSachBaiHatBinding;

import org.greenrobot.eventbus.EventBus;

public class FragmentPlayDanhSachBaiHat extends Fragment implements DanhSachAllChuDeAdapter.OnItemClickedListener {
    private PlayMusicAdapter musicAdapter;
    private FragmentPlayDanhSachBaiHatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayDanhSachBaiHatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (PlayMusicActivity.baiHatList.size() > 0) {
            musicAdapter = new PlayMusicAdapter(getActivity(), PlayMusicActivity.baiHatList, this);
            binding.recyclePlayDanhSachBH.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclePlayDanhSachBH.setAdapter(musicAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClicked(int position) {
        EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, new InformationMusic(PlayMusicActivity.baiHatList.get(0), 0, position)));
    }
}
