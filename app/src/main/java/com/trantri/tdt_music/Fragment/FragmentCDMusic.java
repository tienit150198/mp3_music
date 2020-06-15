package com.trantri.tdt_music.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentCDMusic extends Fragment {
    private View view;
    private CircleImageView mCircleImageView;
    // khi click nó tạo ra các hình ảnh
    private ObjectAnimator mObjectAnimator;

    private boolean aLive = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cd_music, container, false);

        mCircleImageView = view.findViewById(R.id.img_circle);

        mObjectAnimator = ObjectAnimator.ofFloat(mCircleImageView, "rotation", 0f, 360f);
        mObjectAnimator.setDuration(10000);

        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);

        mObjectAnimator.setInterpolator(new LinearInterpolator());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mObjectAnimator.start();
    }

    public void Playnhac(String hinhAnh) {
        Log.d("TAG", hinhAnh);
        try {
            if (!hinhAnh.isEmpty()) {
                Glide.with(Objects.requireNonNull(getContext())).load(hinhAnh).into(mCircleImageView);
            }
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }

    }

    public void stopAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mObjectAnimator.pause();
        }
    }

    public void startAnimation() {
        mObjectAnimator.start();
    }
}
