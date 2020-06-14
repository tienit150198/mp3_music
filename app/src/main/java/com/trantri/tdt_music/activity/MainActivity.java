package com.trantri.tdt_music.activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.Fragment_TimKiem;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "Tìm Kiếm");
        mViewPagerAdapter.addFragment(new FragmentMV(), "Cá Nhân");

        binding.myViewPager.setAdapter(mViewPagerAdapter);
        binding.myTablayout.setupWithViewPager(binding.myViewPager);
        binding.myTablayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        binding.myTablayout.getTabAt(1).setIcon(R.drawable.ic_search);
        binding.myTablayout.getTabAt(2).setIcon(R.drawable.ic_video);
    }

}
