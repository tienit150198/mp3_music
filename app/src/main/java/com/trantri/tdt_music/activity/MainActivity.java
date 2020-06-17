package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.Fragment_TimKiem;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEvent(MessageEventBus message) {
        switch (message.message) {
            case Constraint.EventBusAction.PLAY:
                binding.bottomMusic.getRoot().setVisibility(View.VISIBLE);
                binding.bottomMusic.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                break;
            case Constraint.EventBusAction.PAUSE:
                binding.bottomMusic.getRoot().setVisibility(View.VISIBLE);
                binding.bottomMusic.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                break;
            case Constraint.EventBusAction.PREVIOUS:
                binding.bottomMusic.getRoot().setVisibility(View.VISIBLE);
                break;
            case Constraint.EventBusAction.NEXT:
                binding.bottomMusic.getRoot().setVisibility(View.VISIBLE);
                break;
        }
    }

    private void init() {
        binding.bottomMusic.getRoot().setVisibility(View.GONE);

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "MV");
        mViewPagerAdapter.addFragment(new FragmentMV(), "Cá Nhân");

        binding.myViewPager.setAdapter(mViewPagerAdapter);
        binding.myTablayout.setupWithViewPager(binding.myViewPager);
        Objects.requireNonNull(binding.myTablayout.getTabAt(0)).setIcon(R.drawable.icontrangchu);
        Objects.requireNonNull(binding.myTablayout.getTabAt(1)).setIcon(R.drawable.ic_video);
        Objects.requireNonNull(binding.myTablayout.getTabAt(2)).setIcon(R.drawable.ic_user);
    }

}
