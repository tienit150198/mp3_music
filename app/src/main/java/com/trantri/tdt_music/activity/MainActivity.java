package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.FragmentPlaylist;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void init() {
        binding.bottomSheet.getRoot().setVisibility(View.GONE);

        binding.viewpager.setOffscreenPageLimit(3);
        binding.viewpager.setPagingEnabled(false);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new FragmentMV(), "MV");
        mViewPagerAdapter.addFragment(new FragmentPlaylist(), "Cá Nhân");

        binding.viewpager.setAdapter(mViewPagerAdapter);

        binding.viewpager.setCurrentItem(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private static final String TAG = "LOG_MainActivity";

    @Subscribe
    public void onEvent(MessageEventBus message) {
        switch (message.message) {
            case Constraint.EventBusAction.PLAY:
                binding.bottomSheet.getRoot().setVisibility(View.VISIBLE);
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));

                BaiHatYeuThich mCurrentMusic = (BaiHatYeuThich) message.action;

                if(mCurrentMusic != null){
                    Log.d(TAG, "onEvent: " + mCurrentMusic);
                    Glide.with(this)
                            .load(mCurrentMusic.getHinhBaiHat())
                            .into(binding.bottomSheet.imgMusic);

                    binding.bottomSheet.sheetCasi.setText(mCurrentMusic.getCaSi());
                    binding.bottomSheet.sheetTenbaihat.setText(mCurrentMusic.getTenBaiHat());

                    Log.d(TAG, "onEvent: " + mCurrentMusic.getLuotThich());
                }

                break;
            case Constraint.EventBusAction.PAUSE:
                binding.bottomSheet.getRoot().setVisibility(View.VISIBLE);
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                break;
            case Constraint.EventBusAction.PREVIOUS:
                binding.bottomSheet.getRoot().setVisibility(View.VISIBLE);
                break;
            case Constraint.EventBusAction.NEXT:
                binding.bottomSheet.getRoot().setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeNavigation:

                binding.viewpager.setCurrentItem(0);
                return true;
            case R.id.mvNavigation:

                binding.viewpager.setCurrentItem(1);
                return true;
            case R.id.personNavigation:

                binding.viewpager.setCurrentItem(2);
                return true;

        }
        return false;
    }
}