package com.trantri.tdt_music.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.FragmentPlaylist;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.Fragment.UserFragment;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private ViewPagerAdapter mViewPagerAdapter;

    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setEventClicked();


        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void setEventClicked() {
        binding.bottomSheet.sheetNext.setOnClickListener(v -> EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.NEXT, null)));
        binding.bottomSheet.sheetPlaystate.setOnClickListener(v -> {
            if (isPlay) {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PAUSE, null));
            } else {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.RESUME, null));
            }
        });
    }

    private void init() {
        binding.bottomSheet.getRoot().setVisibility(View.GONE);

        binding.viewpager.setOffscreenPageLimit(3);
        binding.viewpager.setPagingEnabled(false);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new FragmentMV(), "MV");
        mViewPagerAdapter.addFragment(UserFragment.newInstance(), "Cá Nhân");

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
                isPlay = true;
                binding.bottomSheet.getRoot().setVisibility(View.VISIBLE);
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));

                if (message.action != null) {
                    InformationMusic informationMusic = (InformationMusic) message.action;
                    BaiHatYeuThich baiHatYeuThich = informationMusic.getBaiHatYeuThich();

                    if (baiHatYeuThich != null) {
                        setInformationBottomSheet(baiHatYeuThich);
                    }
                }
                break;
            case Constraint.EventBusAction.PAUSE:
                isPlay = false;
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                break;
            case Constraint.EventBusAction.RESUME:
                isPlay = true;
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                break;
            case Constraint.EventBusAction.NEXT:
                isPlay = false;
                break;
            case Constraint.EventBusAction.PREPARED:
                isPlay = true;
                binding.bottomSheet.sheetPlaystate.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                if (message.action != null) {
                    InformationMusic informationMusic = (InformationMusic) message.action;
                    BaiHatYeuThich baiHatYeuThich = informationMusic.getBaiHatYeuThich();

                    if (baiHatYeuThich != null) {
                        setInformationBottomSheet(baiHatYeuThich);
                    }
                }
                break;
            case Constraint.EventBusAction.FAIL:
                Log.d(TAG, "onEvent:  " + message.action);
                Toast.makeText(this, String.valueOf(message.action), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setInformationBottomSheet(@NonNull BaiHatYeuThich mCurrentMusic) {
        Glide.with(this)
                .load(mCurrentMusic.getHinhBaiHat())
                .centerInside()
                .into(binding.bottomSheet.imgMusic);

        binding.bottomSheet.sheetCasi.setText(mCurrentMusic.getCaSi());
        binding.bottomSheet.sheetTenbaihat.setText(mCurrentMusic.getTenBaiHat());
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