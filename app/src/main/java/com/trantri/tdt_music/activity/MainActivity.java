package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trantri.tdt_music.Adapter.ViewPagerAdapter;
import com.trantri.tdt_music.Fragment.FragmentMV;
import com.trantri.tdt_music.Fragment.Fragment_TrangChu;
import com.trantri.tdt_music.Fragment.UserFragment;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivityMainBinding binding;
    private ViewPagerAdapter mViewPagerAdapter;

    private AppDatabase mInstanceDatabase;

    private boolean isPlay = false;

    private BaiHatYeuThich mCurrentBaiHatYeuThich;
    private List<BaiHatYeuThich> mListBaiHatDaThich;

    private Animation animation;
    private int totalTime;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        binding.bottomSheet.imgMusic.startAnimation(animation);

        mCurrentBaiHatYeuThich = null;

        mInstanceDatabase = AppDatabase.getInstance(this);

        mInstanceDatabase.mBaihatBaiHatYeuThichDao().getAllBaiHatYeuThich()
                .observe(this, baiHatYeuThiches -> {
                    mListBaiHatDaThich = baiHatYeuThiches;
                });

        init();
        setEventClicked();


        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void setEventClicked() {
        binding.bottomSheet.sheetFravorite.setOnClickListener(v -> {
            boolean isLiked = false;

            for (int i = 0; i < mListBaiHatDaThich.size(); i++) {
                BaiHatYeuThich baiHatYeuThich = mListBaiHatDaThich.get(i);
                if (baiHatYeuThich.getTenBaiHat().equals(mCurrentBaiHatYeuThich.getTenBaiHat())) {
                    if (baiHatYeuThich.isLiked()) {
                        mCurrentBaiHatYeuThich.setLiked(false);
                        mInstanceDatabase.mBaihatBaiHatYeuThichDao().deleteBaiHatYeuThich(baiHatYeuThich);
                        binding.bottomSheet.sheetFravorite.setChecked(false);
                        isLiked = true;
                    }
                }
            }
            if (!isLiked) {
                binding.bottomSheet.sheetFravorite.setChecked(true);
                mCurrentBaiHatYeuThich.setLiked(true);
                Toast.makeText(this, "Đã thích", Toast.LENGTH_SHORT).show();
                mInstanceDatabase.mBaihatBaiHatYeuThichDao().insertBaiHatYeuThich(mCurrentBaiHatYeuThich);
            }

            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.UPDATE, mCurrentBaiHatYeuThich));
        });
        binding.bottomSheet.sheetNext.setOnClickListener(v -> EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.NEXT, null)));
        binding.bottomSheet.sheetPlaystate.setOnClickListener(v -> {
            if (isPlay) {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PAUSE, null));
            } else {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.RESUME, null));
            }
        });

        binding.bottomSheet.rlShowMusic.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayMusicActivity.class);
            intent.putExtra(Constraint.SHOW_SCREEN, Boolean.TRUE);
            intent.putExtra(Constraint.TOTAL_TIME, totalTime);
            intent.putExtra(Constraint.POSITION, currentPosition);

            startActivity(intent);
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

        // config refresh data
        binding.refresh.setOnRefreshListener(this);

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
                    totalTime = informationMusic.getDurationTime();
                    currentPosition = informationMusic.getPosition();
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
                    totalTime = informationMusic.getDurationTime();
                    currentPosition = informationMusic.getPosition();

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
        mCurrentBaiHatYeuThich = mCurrentMusic;
        binding.bottomSheet.sheetFravorite.setChecked(mCurrentBaiHatYeuThich.isLiked());
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

    @Override
    public void onRefresh() {
        binding.refresh.setRefreshing(true);

        new Handler().postDelayed(() -> {
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PAUSE, null));
            Toast.makeText(this, "data refreshed", Toast.LENGTH_SHORT).show();
            binding.refresh.setRefreshing(false);
        }, 1000);
    }
}