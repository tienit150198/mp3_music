package com.trantri.tdt_music.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.databinding.FragmentCdMusicBinding;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentCDMusic extends Fragment {
    private FragmentCdMusicBinding binding;
    private Animation animation;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FragmentCDMusic() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCdMusicBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    private static final String TAG = "111";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation);

        compositeDisposable.add(
                PlayMusicActivity
                        .getImageSubject()
                        .subscribeOn(Schedulers.computation())
                        .subscribe(s -> {
                            if (s != null && !s.isEmpty()) {
                                playMusic(s);
                            }
                        })
        );

    }

    public void playMusic(String hinhAnh) {
        Log.d(TAG, hinhAnh + " - " + (getActivity() != null));
        try {
            if (getActivity() != null) {
                Glide
                        .with(this)
                        .load(hinhAnh)
                        .into(binding.imgCircle);
                binding.imgCircle.startAnimation(animation);
            }

        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
    }

    public void stopAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.imgCircle.clearAnimation();
        }
    }
}
