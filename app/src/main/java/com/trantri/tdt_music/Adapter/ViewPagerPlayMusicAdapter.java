package com.trantri.tdt_music.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerPlayMusicAdapter extends FragmentPagerAdapter {

    public List<Fragment> fragmentArrayList;

    public ViewPagerPlayMusicAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentArrayList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return (fragmentArrayList != null ? fragmentArrayList.size() : 0);
    }

}
