package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.trantri.tdt_music.Adapter.ViewPagerPlayMusicAdapter;
import com.trantri.tdt_music.Fragment.FragmentCDMusic;
import com.trantri.tdt_music.Fragment.FragmentPlayDanhSachBaiHat;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.ActivityPlayMusicBinding;
import com.trantri.tdt_music.service.PlayMusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlayMusicActivity extends AppCompatActivity {

    public static ArrayList<BaiHatYeuThich> baiHatList = new ArrayList<>();

    public static ViewPagerPlayMusicAdapter mViewPagerPlayMusicAdapter;
    private ActivityPlayMusicBinding binding;

    private FragmentCDMusic mFragmentCDMusic;

    private FragmentPlayDanhSachBaiHat mFragmentPlayDanhSachBaiHat;

    private MediaPlayer mMediaPlayer;


    //TODO: tran tien
    private int mCurrentTime = 0;
    private int mCurrentPosition = 0;
    private boolean isPlay = false;

    private boolean isLoop = false;
    private boolean isSuffix = false;

    int position = 0;

    boolean repeat = false;

    boolean checkRandom = false;

    boolean next = false;

    private Handler mHandler = null;

    private static final String TAG = "xxxx PlayMusicActivity";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EventBus.getDefault().register(this);
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//khởi tạo handler
        mHandler = new Handler();

        GetDataFromItent();

        Intent intentService = new Intent(this, PlayMusicService.class);
        intentService.putParcelableArrayListExtra("allbaihat", baiHatList);
        startService(intentService);

        init();
        eventClick();

        compositeDisposable.add(
                PlayMusicService.getCurrentTime()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> mCurrentTime = integer)
        );


        //khởi tạo bài hát dầu tiên

        //set title action bar
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
            case Constraint.EventBusAction.FAIL:
                Log.d(TAG, "onEvent: " + messageEventBus.action);
                    Toast.makeText(this, String.valueOf(messageEventBus.action), Toast.LENGTH_SHORT).show();
                break;
            case Constraint.EventBusAction.PREPARED:
                if (messageEventBus.action != null) {
                    InformationMusic mInformationMusic = (InformationMusic) messageEventBus.action;
                    TimeSong(mInformationMusic.getDurationTime());
                    UpdateTime();

                    Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(mInformationMusic.getPosition()).getTenBaiHat());
                }
                break;

        }
    }

    private void resumeMusic() {
        binding.btnPlay.setImageResource(R.drawable.iconpause);
        isPlay = true;
    }

    private void playMusic() {
        isPlay = true;
        Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(mCurrentPosition).getTenBaiHat());
        binding.btnPlay.setImageResource(R.drawable.iconpause);

//        mFragmentCDMusic.Playnhac(baiHatList.get(mCurrentPosition).getHinhBaiHat());
        // start Animation
        mFragmentCDMusic.startAnimation();
    }

    private void pauseMusic() {
        isPlay = false;
        binding.btnPlay.setImageResource(R.drawable.iconplay);
        mFragmentCDMusic.stopAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        EventBus.getDefault().unregister(this);
    }

    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPagerPlayMusicAdapter.getItem(1);
                if (!baiHatList.isEmpty()) {

//                    mFragmentCDMusic.Playnhac(baiHatList.get(mCurrentPosition).getHinhBaiHat());
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 300);
                }

            }
        }, 500);

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
            if(isLoop){
                Toast.makeText(this, "LOOP ON", Toast.LENGTH_SHORT).show();
                binding.btnLapLai.setColorFilter(ContextCompat.getColor(this,R.color.playmusic_selected));
            }else{
                Toast.makeText(this, "LOOP OFF", Toast.LENGTH_SHORT).show();
                binding.btnLapLai.setColorFilter(ContextCompat.getColor(this,R.color.playmusic_unselect));
            }
//            if (!repeat) {
//                if (checkRandom) {
//                    checkRandom = false;
//                    binding.btnLapLai.setImageResource(R.drawable.iconsyned);
//                    binding.btnRandom.setImageResource(R.drawable.iconsuffle);
//                }
//                binding.btnLapLai.setImageResource(R.drawable.iconsyned);
//                repeat = true;
//            } else {
//                binding.btnLapLai.setImageResource(R.drawable.iconrepeat);
//                repeat = false;
//            }
        });
        binding.btnRandom.setOnClickListener(v -> {
            isSuffix = !isSuffix;
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.SUFFIX, isSuffix));

            if(isSuffix){
                Toast.makeText(this, "RANDOM ON", Toast.LENGTH_SHORT).show();
                binding.btnRandom.setColorFilter(ContextCompat.getColor(this,R.color.playmusic_selected));
            }else{
                Toast.makeText(this, "RANDOM OFF", Toast.LENGTH_SHORT).show();
                binding.btnRandom.setColorFilter(ContextCompat.getColor(this,R.color.playmusic_unselect));
            }
//            if (!checkRandom) {
//                if (repeat) {
//                    repeat = false;
//                    binding.btnRandom.setImageResource(R.drawable.iconshuffled);
//                    binding.btnLapLai.setImageResource(R.drawable.iconrepeat);
//                }
//                binding.btnRandom.setImageResource(R.drawable.iconshuffled);
//                checkRandom = true;
//            } else {
//                binding.btnRandom.setImageResource(R.drawable.iconsuffle);
//                checkRandom = false;
//            }
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
        binding.btnNext.setOnClickListener(v -> {
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.NEXT, null));

//            if (baiHatList.size() > 0) {
//                if (mMediaPlayer.isPlaying() || mMediaPlayer != null) {
//                    mMediaPlayer.stop();
//                    mMediaPlayer.release();
//                    mMediaPlayer = null;
//                }
//                if (position < (baiHatList.size())) {
//                    binding.btnPlay.setImageResource(R.drawable.iconpause);
//                    EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, baiHatList.get(position)));
//
//                    position++;
//
//
//                    if (repeat) {
//                        if (position == 0) {
//                            position = baiHatList.size();
//                        }
//                        position -= 1;
//                    }
//                    if (checkRandom) {
//                        Random random = new Random();
//                        int index = random.nextInt(baiHatList.size());
//                        if (index == position) {
//                            position = index - 1;
//                        }
//                        position = index;
//                    }
//                    if (position > (baiHatList.size()) - 1) {
//                        position = 0;
//                    }
//                    new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
//                    mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
//                    Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(position).getTenBaiHat());
//                    UpdateTime();
//                }
//            }
//            binding.btnBack.setClickable(false);
//            binding.btnNext.setClickable(false);
//            Handler mHandler = new Handler();
//            mHandler.postDelayed(() -> {
//                binding.btnBack.setClickable(true);
//                binding.btnNext.setClickable(true);
//            }, 5000);
        });
        binding.btnBack.setOnClickListener(v -> {
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PREVIOUS, null));

//            if (baiHatList.size() > 0) {
//                if (mMediaPlayer.isPlaying() || mMediaPlayer != null) {
//                    mMediaPlayer.stop();
//                    mMediaPlayer.release();
//                    mMediaPlayer = null;
//                }
//                if (position < (baiHatList.size())) {
//
//                    binding.btnPlay.setImageResource(R.drawable.iconpause);
//                    EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, baiHatList.get(position)));
//
//                    position--;
//
//                    if (position < 0) {
//
//                        position = baiHatList.size() - 1;
//
//                    }
//                    if (repeat) {
//                        position += 1;
//                    }
//                    if (checkRandom) {
//                        Random random = new Random();
//                        int index = random.nextInt(baiHatList.size());
//                        if (index == position) {
//                            position = index - 1;
//                        }
//                        position = index;
//                    }
//
//                    new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
//                    mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
//                    Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(position).getTenBaiHat());
//                    UpdateTime();
//                }
//            }
            //Test máy thật xem như nhau mà t test từ hôm qua r
//            binding.btnBack.setClickable(false);
//            binding.btnNext.setClickable(false);
//            Handler mHandler = new Handler();
//            mHandler.postDelayed(() -> {
//                binding.btnBack.setClickable(true);
//                binding.btnNext.setClickable(true);
//            }, 500);
        });
    }

    private void GetDataFromItent() {
        Intent intent = getIntent();
        baiHatList.clear();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                Log.d(TAG, "GetDataFromItent: cakhuc");
                BaiHatYeuThich baiHatYeuThich = intent.getParcelableExtra("cakhuc");
                baiHatList.add(baiHatYeuThich);
            }
            if (intent.hasExtra("allbaihat")) {
                Log.d(TAG, "GetDataFromItent: allbaihat");
                baiHatList = intent.getParcelableArrayListExtra("allbaihat");
            }


            Log.d(TAG, "GetDataFromItent: " + (baiHatList != null ? baiHatList.toString() : null));
        }

    }

    private void init() {
        setSupportActionBar(binding.toobarPlayNhac);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toobarPlayNhac.setNavigationOnClickListener(v -> {
            finish();
//            mMediaPlayer.stop();
            baiHatList.clear();
        });
        binding.toobarPlayNhac.setTitleTextColor(Color.WHITE);
        mFragmentCDMusic = new FragmentCDMusic();
        mFragmentPlayDanhSachBaiHat = new FragmentPlayDanhSachBaiHat();

        mViewPagerPlayMusicAdapter = new ViewPagerPlayMusicAdapter(getSupportFragmentManager());

        mViewPagerPlayMusicAdapter.addFragment(mFragmentPlayDanhSachBaiHat);

        mViewPagerPlayMusicAdapter.addFragment(mFragmentCDMusic);

        binding.viewPagerPlayNhac.setAdapter(mViewPagerPlayMusicAdapter);
        mFragmentCDMusic = (FragmentCDMusic) mViewPagerPlayMusicAdapter.getItem(1);

        if (baiHatList != null && baiHatList.size() > 0) {
            getSupportActionBar().setTitle(baiHatList.get(0).getTenBaiHat());
//            new PlayMusic().execute(baiHatList.get(0).getLinkBaiHat());
//            binding.btnPlay.setImageResource(R.drawable.iconpause);
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, new InformationMusic(baiHatList.get(0), 0, 0)));
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
//            }
        }, 100);

//        final Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (next) {
//                    if (position < (baiHatList.size())) {
//                        binding.btnPlay.setImageResource(R.drawable.iconpause);
//                        EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PLAY, baiHatList.get(position)));
//                        position++;
//                        if (repeat) {
//                            if (position == 0) {
//                                position = baiHatList.size();
//                            }
//                            position -= 1;
//                        }
//                        if (checkRandom) {
//                            Random random = new Random();
//                            int index = random.nextInt(baiHatList.size());
//                            if (index == position) {
//                                position = index - 1;
//                            }
//                            position = index;
//                        }
//                        if (position > (baiHatList.size())) {
//                            position = 0;
//                        }
//                        new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
//                        mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
//                        Objects.requireNonNull(getSupportActionBar()).setTitle(baiHatList.get(position).getTenBaiHat());
//                    }
//
//                    binding.btnBack.setClickable(false);
//                    binding.btnBack.setClickable(false);
//                    Handler mHandler = new Handler();
//                    mHandler.postDelayed(() -> {
//                        binding.btnBack.setClickable(true);
//                        binding.btnNext.setClickable(true);
//                    }, 5000);
//                    next = false;
//                    handler1.removeCallbacks(this);
//
//                } else {
//
//                    handler1.postDelayed(this, 1000);
//                }
//            }
//        }, 1000);
//    }

//    class PlayMusic extends AsyncTask<String, Void, String> {
//        // khi thực hiện
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//
//        // trả kết quả
//        @Override
//        protected void onPostExecute(String baihat) {
//            try {
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // play dưới dạng online
//                mMediaPlayer.setOnCompletionListener(mp -> {
//                    mMediaPlayer.stop();
//                    mMediaPlayer.reset();
//                });
//                mMediaPlayer.setDataSource(baihat);
//                // khi media muốn phát đc
//                mMediaPlayer.prepareAsync();
//                mMediaPlayer.setOnPreparedListener(mp -> {
//                    mp.start();
//                    TimeSong();
//                    UpdateTime();
//                });
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
    }
}