package com.trantri.tdt_music.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trantri.tdt_music.Adapter.ViewPagerPlayMusicAdapter;
import com.trantri.tdt_music.Fragment.FragmentCDMusic;
import com.trantri.tdt_music.Fragment.FragmentPlayDanhSachBaiHat;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.data.local.PlayListUser;
import com.trantri.tdt_music.databinding.ActivityPlayMusicBinding;
import com.trantri.tdt_music.helper.DialogHelper;
import com.trantri.tdt_music.service.PlayMusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class PlayMusicActivity extends AppCompatActivity {

    public static List<BaiHatYeuThich> baiHatList = new ArrayList<>();

    public static ViewPagerPlayMusicAdapter mViewPagerPlayMusicAdapter;
    private ActivityPlayMusicBinding binding;

    private FragmentCDMusic mFragmentCDMusic;

    private FragmentPlayDanhSachBaiHat mFragmentPlayDanhSachBaiHat;

    private static BehaviorSubject<String> mImageSubject = BehaviorSubject.createDefault("");

    //TODO: tran tien
    private int mCurrentTime = 0;
    private int mCurrentPosition = 0;
    private boolean isPlay = false;

    private boolean isLoop = false;
    private boolean isSuffix = false;

    private static final String TAG = "111";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AppDatabase mInstanceDatabase;

    private List<BaiHatYeuThich> mListBaiHatDaThich;
    private List<PlayListUser> mListPlayListUsers;

    private boolean isShowReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EventBus.getDefault().register(this);
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mInstanceDatabase = AppDatabase.getInstance(this);

        mInstanceDatabase.mBaihatBaiHatYeuThichDao()
                .getAllBaiHatYeuThich().observe(this, baiHatYeuThiches -> mListBaiHatDaThich = baiHatYeuThiches);
        mInstanceDatabase.mPlayListUserDao()
                .getAllPlaylist().observe(this, playListUsers -> mListPlayListUsers = playListUsers);

        GetDataFromItent();

        init();
        eventClick();

        if (!isShowReturn) {
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.RESUME, null));
            Intent intentService = new Intent(this, PlayMusicService.class);
            intentService.putExtra("allbaihat", new Gson().toJson(baiHatList));
            startService(intentService);
        }

        compositeDisposable.add(
                PlayMusicService.getCurrentTime()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            mCurrentTime = integer;
                            UpdateTime();
                        })
        );
    }

    public static BehaviorSubject<String> getImageSubject() {
        return mImageSubject;
    }

    @Subscribe
    public void onEvent(MessageEventBus messageEventBus) {
        switch (messageEventBus.message) {
            case Constraint
                    .EventBusAction.PLAY:
                if (messageEventBus.action != null) {
                    Log.d(TAG, "onEvent: " + messageEventBus.action.toString());
                    mCurrentPosition = ((InformationMusic) messageEventBus.action).getPosition();
                }

                playMusic();
                break;
            case Constraint
                    .EventBusAction.PAUSE:
                pauseMusic();
                break;
            case Constraint
                    .EventBusAction.RESUME:
                resumeMusic();
                break;
            case Constraint.EventBusAction.NEXT:
                binding.btnPlay.setImageResource(R.drawable.iconplay);

                break;
            case Constraint.EventBusAction.FAIL:
                Log.d(TAG, "onEvent: " + messageEventBus.action);
                Toast.makeText(this, String.valueOf(messageEventBus.action), Toast.LENGTH_SHORT).show();
                break;
            case Constraint.EventBusAction.PREPARED:
                if (messageEventBus.action != null) {
                    InformationMusic mInformationMusic = (InformationMusic) messageEventBus.action;
                    TimeSong(mInformationMusic.getDurationTime());
                    UpdateTime();

                    mCurrentPosition = mInformationMusic.getPosition();
                    binding.btnPlay.setImageResource(R.drawable.iconpause);
                    mImageSubject.onNext(mInformationMusic.getBaiHatYeuThich().getHinhBaiHat());
                    Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(mInformationMusic.getPosition()).getTenBaiHat());
                }
                break;

        }
    }

    private void resumeMusic() {
        Log.d(TAG, "resumeMusic: ");
        binding.btnPlay.setImageResource(R.drawable.iconpause);
        isPlay = true;

        binding.imgFavorite.setChecked(baiHatList.get(mCurrentPosition).isLiked());
        mImageSubject.onNext(baiHatList.get(mCurrentPosition).getHinhBaiHat());

        Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(mCurrentPosition).getTenBaiHat());
    }

    private void playMusic() {
        Log.d(TAG, "playMusic: ");
        isPlay = true;
        binding.imgFavorite.setChecked(baiHatList.get(mCurrentPosition).isLiked());
        Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(mCurrentPosition).getTenBaiHat());
        binding.btnPlay.setImageResource(R.drawable.iconpause);

        mImageSubject.onNext(baiHatList.get(mCurrentPosition).getHinhBaiHat());
    }

    private void pauseMusic() {
        Log.d(TAG, "pauseMusic: ");
        isPlay = false;
        binding.imgFavorite.setChecked(baiHatList.get(mCurrentPosition).isLiked());
        binding.btnPlay.setImageResource(R.drawable.iconplay);
        mFragmentCDMusic.stopAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
//        EventBus.getDefault().unregister(this);
    }

    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPagerPlayMusicAdapter.getItem(1);
                if (!baiHatList.isEmpty()) {
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 300);
                }
            }
        }, 500);

        binding.tvPlayMusic.setOnClickListener(v -> {
            ArrayList<String> listNamePlaylist = new ArrayList<>();
            for (PlayListUser playListUser : mListPlayListUsers) {
                listNamePlaylist.add(playListUser.getName());
            }

            listNamePlaylist.add("new playlist");

            DialogHelper.showListDialog(this, listNamePlaylist, 0, R.string.title_dialog_selected, R.string.OK, R.string.Cancel, new DialogHelper.OnSelectedDialogListener() {
                @Override
                public void onPositiveButton(DialogInterface dialog, int which) {
                    Log.d(TAG, "onPositiveButton: " + listNamePlaylist.get(which));
                    dialog.dismiss();

                    if (which == listNamePlaylist.size() - 1) {
                        DialogHelper.showDialogInput(PlayMusicActivity.this, R.string.title_dialog_input, R.string.message_dialog_input, R.string.OK, R.string.Cancel, new DialogHelper.OnSelectedDialogListener() {
                            @Override
                            public void onPositiveButton(DialogInterface dialog, String message) {
                                if (message != null && !message.isEmpty()) {
                                    PlayListUser playlistUser = new PlayListUser();

                                    playlistUser.setName(message);
                                    playlistUser.setListBaiHatYeuThich(Collections.singletonList(baiHatList.get(mCurrentPosition)));

                                    mInstanceDatabase.mPlayListUserDao().insertPlaylist(playlistUser);
                                    Toast.makeText(PlayMusicActivity.this, "Add success fully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(PlayMusicActivity.this, "name playlist not empty", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNegativeButton(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        String nameSelected = listNamePlaylist.get(which);
                        for (int i = 0; i < mListPlayListUsers.size(); i++) {
                            if (mListPlayListUsers.get(i).getName().equals(nameSelected)) {
                                PlayListUser playListUser = mListPlayListUsers.get(i);

                                List<BaiHatYeuThich> baiHatYeuThichList = playListUser.getListBaiHatYeuThich();
                                baiHatYeuThichList.add(baiHatList.get(mCurrentPosition));

                                playListUser.setListBaiHatYeuThich(baiHatYeuThichList);
                                mInstanceDatabase.mPlayListUserDao().updatePlaylist(playListUser);

                                Toast.makeText(PlayMusicActivity.this, "Add success fully", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }

                }

                @Override
                public void onNegativeButton(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        });

        binding.imgFavorite.setOnClickListener(v -> {
            boolean isLiked = false;

            for (int i = 0; i < mListBaiHatDaThich.size(); i++) {
                BaiHatYeuThich baiHatYeuThich = mListBaiHatDaThich.get(i);
                if (baiHatYeuThich.getTenBaiHat().equals(baiHatList.get(mCurrentPosition).getTenBaiHat())) {
                    if (baiHatYeuThich.isLiked()) {
                        baiHatList.get(mCurrentPosition).setLiked(false);
                        mInstanceDatabase.mBaihatBaiHatYeuThichDao().deleteBaiHatYeuThich(baiHatYeuThich);
                        binding.imgFavorite.setChecked(false);
                        isLiked = true;
                    }
                }
            }
            if (!isLiked) {
                binding.imgFavorite.setChecked(true);
                baiHatList.get(mCurrentPosition).setLiked(true);
                Toast.makeText(this, "Đã thích", Toast.LENGTH_SHORT).show();
                mInstanceDatabase.mBaihatBaiHatYeuThichDao().insertBaiHatYeuThich(baiHatList.get(mCurrentPosition));
            }

            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.UPDATE, baiHatList.get(mCurrentPosition)));
        });

        binding.btnPlay.setOnClickListener(v -> {
            if (isPlay) {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PAUSE, null));
            } else {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.RESUME, null));
            }
        });

        binding.btnLapLai.setOnClickListener(v -> {
            isLoop = !isLoop;

            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.LOOP, isLoop));
            if (isLoop) {
                Toast.makeText(this, "LOOP ON", Toast.LENGTH_SHORT).show();
                binding.btnLapLai.setColorFilter(ContextCompat.getColor(this, R.color.playmusic_selected));
            } else {
                Toast.makeText(this, "LOOP OFF", Toast.LENGTH_SHORT).show();
                binding.btnLapLai.setColorFilter(ContextCompat.getColor(this, R.color.playmusic_unselect));
            }
        });
        binding.btnRandom.setOnClickListener(v -> {
            isSuffix = !isSuffix;
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.SUFFIX, isSuffix));

            if (isSuffix) {
                Toast.makeText(this, "RANDOM ON", Toast.LENGTH_SHORT).show();
                binding.btnRandom.setColorFilter(ContextCompat.getColor(this, R.color.playmusic_selected));
            } else {
                Toast.makeText(this, "RANDOM OFF", Toast.LENGTH_SHORT).show();
                binding.btnRandom.setColorFilter(ContextCompat.getColor(this, R.color.playmusic_unselect));
            }
        });
        binding.seekbarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.SEEK, binding.seekbarSong.getProgress()));
//                mMediaPlayer.seekTo(binding.seekbarSong.getProgress());
            }
        });
        binding.btnNext.setOnClickListener(v ->
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.NEXT, null)));
        binding.btnBack.setOnClickListener(v ->
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PREVIOUS, null)));
    }

    private void GetDataFromItent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constraint.SHOW_SCREEN) && intent.getBooleanExtra(Constraint.SHOW_SCREEN, false)) {
                isShowReturn = true;
                TimeSong(intent.getIntExtra(Constraint.TOTAL_TIME, 0));
                mCurrentPosition = intent.getIntExtra(Constraint.POSITION, 0);
                return;
            }
            baiHatList.clear();
            if (intent.hasExtra("cakhuc")) {
                String text = intent.getStringExtra("cakhuc");
                BaiHatYeuThich baiHatYeuThich = new Gson().fromJson(text, BaiHatYeuThich.class);
                baiHatList.add(baiHatYeuThich);
            }
            if (intent.hasExtra("allbaihat")) {
                String txtAllBaiHat = intent.getStringExtra("allbaihat");
                Type type = new TypeToken<List<BaiHatYeuThich>>() {
                }.getType();

                baiHatList = new Gson().fromJson(txtAllBaiHat, type);
            }
        }
    }

    private void init() {
        setSupportActionBar(binding.toobarPlayNhac);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toobarPlayNhac.setNavigationOnClickListener(v -> {
            finish();
            baiHatList.clear();
        });
        binding.toobarPlayNhac.setTitleTextColor(Color.WHITE);

        List<Fragment> fragmentList = new ArrayList<>();

        mFragmentPlayDanhSachBaiHat = new FragmentPlayDanhSachBaiHat();
        mFragmentCDMusic = new FragmentCDMusic();

        fragmentList.add(mFragmentPlayDanhSachBaiHat);
        fragmentList.add(mFragmentCDMusic);

        mViewPagerPlayMusicAdapter = new ViewPagerPlayMusicAdapter(getSupportFragmentManager(), fragmentList);
        binding.viewPagerPlayNhac.setAdapter(mViewPagerPlayMusicAdapter);

        binding.tabLayout.setupWithViewPager(binding.viewPagerPlayNhac, true);

        if (baiHatList != null && baiHatList.size() > 0) {

            if (!isShowReturn) {
                getSupportActionBar().setTitle(baiHatList.get(0).getTenBaiHat());
                EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, new InformationMusic(baiHatList.get(0), 0, 0)));
            }else{
                getSupportActionBar().setTitle(baiHatList.get(mCurrentPosition).getTenBaiHat());
                binding.btnPlay.setImageResource(R.drawable.iconpause);
            }
        }
    }

    public void TimeSong(int totalTime) {
        try {
            SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");
            binding.tvTotalTimeSong.setText(mFormat.format(totalTime));
            binding.seekbarSong.setMax(totalTime);
        } catch (Exception e) {
            Log.d("TAG", "TimeSong: " + e);
        }

    }

    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
                // thời gian vị trí hiện tai của bài hát
                binding.tvTimeSong.setText(formatTime.format(mCurrentTime));
                // update process skSong
                binding.seekbarSong.setProgress(mCurrentTime);
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
}