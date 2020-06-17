package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.FragmentPlaylist;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void init() {
        binding.viewpager.setOffscreenPageLimit(3);
        binding.viewpager.setPagingEnabled(false);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new FragmentMV(), "MV");
        mViewPagerAdapter.addFragment(new FragmentPlaylist(), "Cá Nhân");

        binding.viewpager.setAdapter(mViewPagerAdapter);

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