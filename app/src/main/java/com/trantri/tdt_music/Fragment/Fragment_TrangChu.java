package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trantri.tdt_music.databinding.FragmentTrangChuBinding;

public class Fragment_TrangChu extends Fragment {
    FragmentTrangChuBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrangChuBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
