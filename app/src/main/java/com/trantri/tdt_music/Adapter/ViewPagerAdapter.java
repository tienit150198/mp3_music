package com.trantri.tdt_music.Adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();

    private ArrayList<String> arrayTitle = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        arrayTitle.add(title);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Nullable
    @Override
    // tên của mỗi cái page
    public CharSequence getPageTitle(int position) {
        return arrayTitle.get(position);
    }
}
