package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.trantri.tdt_music.Adapter.ViewPagerPlayMusicAdapter;
import com.trantri.tdt_music.Fragment.FragmentCDMusic;
import com.trantri.tdt_music.Fragment.FragmentPlayDanhSachBaiHat;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.databinding.ActivityPlayMusicBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity {

    public static ArrayList<BaiHatYeuThich> baiHatList = new ArrayList<BaiHatYeuThich>();

    public static ViewPagerPlayMusicAdapter mViewPagerPlayMusicAdapter;
    ActivityPlayMusicBinding binding;

    ViewPager mViewPager;



    SeekBar mSeekBar;

    FragmentCDMusic mFragmentCDMusic;

    FragmentPlayDanhSachBaiHat mFragmentPlayDanhSachBaiHat;

    MediaPlayer mMediaPlayer;

    int position = 0;

    boolean repeat = false;

    boolean checkRandom = false;

    boolean next = false;

    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//khởi tạo handler
        mHandler = new Handler();

        init();

        eventClick();

        GetDataFromItent();

        //khởi tạo bài hát dầu tiên

       try {
           mMediaPlayer = new MediaPlayer();
           mMediaPlayer.setDataSource(this, Uri.parse(baiHatList.get(0).getLinkBaiHat()));
           mMediaPlayer.prepareAsync();
           mMediaPlayer.setOnPreparedListener(mp -> {
               mp.start();
               TimeSong();
               UpdateTime();
               binding.btnPlay.setImageResource(mMediaPlayer.isPlaying() ? R.drawable.iconpause :
                       R.drawable.iconplay);
           });

       } catch (IllegalArgumentException | IOException e){
           Log.d("TAG", "erross"+e);
       }
        //set title action bar
        getSupportActionBar().setTitle(baiHatList.get(0).getTenBaiHat());
    }


    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mViewPagerPlayMusicAdapter.getItem(1) != null) {
                    if (!baiHatList.isEmpty()) {
                        mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }

                }

            }
        }, 500);

        binding.btnPlay.setOnClickListener(v -> {

            if (mMediaPlayer.isPlaying()) {

                mMediaPlayer.pause();

                binding.btnPlay.setImageResource(R.drawable.iconplay);
                mFragmentCDMusic.stopAnimation();

            } else {
                mMediaPlayer.start();
                binding.btnPlay.setImageResource(R.drawable.iconpause);
                mFragmentCDMusic.startAnimation();
            }
            TimeSong();
            UpdateTime();
        });

        binding.btnLapLai.setOnClickListener(v -> {
            if (repeat == false) {
                if (checkRandom == true) {
                    checkRandom = false;
                    binding.btnLapLai.setImageResource(R.drawable.iconsyned);
                    binding.btnRandom.setImageResource(R.drawable.iconsuffle);
                }
                binding.btnLapLai.setImageResource(R.drawable.iconsyned);
                repeat = true;
            } else {
                binding.btnLapLai.setImageResource(R.drawable.iconrepeat);
                repeat = false;
            }
        });
        binding.btnRandom.setOnClickListener(v -> {
            if (checkRandom == false) {
                if (repeat == true) {
                    repeat = false;
                    binding.btnRandom.setImageResource(R.drawable.iconshuffled);
                    binding.btnLapLai.setImageResource(R.drawable.iconrepeat);
                }
                binding.btnRandom.setImageResource(R.drawable.iconshuffled);
                checkRandom = true;
            } else {
                binding.btnRandom.setImageResource(R.drawable.iconsuffle);
                checkRandom = false;
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(mSeekBar.getProgress());
            }
        });
        binding.btnNext.setOnClickListener(v -> {
            if (baiHatList.size() > 0) {
                if (mMediaPlayer.isPlaying() || mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                if (position < (baiHatList.size())) {
                    binding.btnPlay.setImageResource(R.drawable.iconpause);
                    position++;


                    if (repeat == true) {
                        if (position == 0) {
                            position = baiHatList.size();
                        }
                        position -= 1;
                    }
                    if (checkRandom == true) {
                        Random random = new Random();
                        int index = random.nextInt(baiHatList.size());
                        if (index == position) {
                            position = index - 1;
                        }
                        position = index;
                    }
                    if (position > (baiHatList.size()) - 1) {
                        position = 0;
                    }
                    new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
                    mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                    getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
                    UpdateTime();
                }
            }
            binding.btnBack.setClickable(false);
            binding.btnNext.setClickable(false);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.btnBack.setClickable(true);
                    binding.btnNext.setClickable(true);
                }
            }, 5000);
        });
        binding.btnBack.setOnClickListener(v -> {
            if (baiHatList.size() > 0) {
                if (mMediaPlayer.isPlaying() || mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                if (position < (baiHatList.size())) {

                    binding.btnPlay.setImageResource(R.drawable.iconpause);

                    position--;

                    if (position < 0) {

                        position = baiHatList.size() - 1;

                    }
                    if (repeat == true) {
                        position += 1;
                    }
                    if (checkRandom == true) {
                        Random random = new Random();
                        int index = random.nextInt(baiHatList.size());
                        if (index == position) {
                            position = index - 1;
                        }
                        position = index;
                    }

                    new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
                    mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                    getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
                    UpdateTime();
                }
            }
            //Test máy thật xem như nhau mà t test từ hôm qua r
            binding.btnBack.setClickable(false);
            binding.btnNext.setClickable(false);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.btnBack.setClickable(true);
                    binding.btnNext.setClickable(true);
                }
            }, 500);
        });
    }

    private void GetDataFromItent() {
        Intent intent = getIntent();
        baiHatList.clear();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                BaiHatYeuThich baiHatYeuThich = intent.getParcelableExtra("cakhuc");
                baiHatList.add(baiHatYeuThich);
            }
            if (intent.hasExtra("allbaihat")) {
                ArrayList<BaiHatYeuThich> allbaihatList = intent.getParcelableArrayListExtra("allbaihat");
                baiHatList = allbaihatList;
            }
        }

    }

    private void init() {
        setSupportActionBar(binding.toobarPlayNhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toobarPlayNhac.setNavigationOnClickListener(v -> {
            finish();
            mMediaPlayer.stop();
            baiHatList.clear();
        });
        binding.toobarPlayNhac.setTitleTextColor(Color.WHITE);
        mFragmentCDMusic = new FragmentCDMusic();
        mFragmentPlayDanhSachBaiHat = new FragmentPlayDanhSachBaiHat();

        mViewPagerPlayMusicAdapter = new ViewPagerPlayMusicAdapter(getSupportFragmentManager());

        mViewPagerPlayMusicAdapter.addFragment(mFragmentPlayDanhSachBaiHat);

        mViewPagerPlayMusicAdapter.addFragment(mFragmentCDMusic);

        mViewPager.setAdapter(mViewPagerPlayMusicAdapter);
        mFragmentCDMusic = (FragmentCDMusic) mViewPagerPlayMusicAdapter.getItem(1);

        if (baiHatList.size() > 0) {
            getSupportActionBar().setTitle(baiHatList.get(0).getTenBaiHat());
            new PlayMusic().execute(baiHatList.get(0).getLinkBaiHat());
            binding.btnPlay.setImageResource(R.drawable.iconpause);
        }
    }
    public void TimeSong() {
        try{
            SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");
            if (mMediaPlayer.getDuration() > 0){
                binding.tvTotalTimeSong.setText(mFormat.format(mMediaPlayer.getDuration()));
                mSeekBar.setMax(mMediaPlayer.getDuration());
            }
        } catch (Exception e){
            Log.d("TAG", "TimeSong: "+e);
        }

    }

    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
// thời gian vị trí hiện tai của bài hast
                    binding.tvTimeSong.setText(formatTime.format(mMediaPlayer.getCurrentPosition()));
                    // update process skSong
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    // kiểm tra thời gian bài hát nếu kết thúc -> next
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (position < (baiHatList.size())) {
                                binding.btnPlay.setImageResource(R.drawable.iconpause);
                                position++;
                                if (repeat == true) {
                                    if (position == 0) {
                                        position = baiHatList.size();
                                    }
                                    position -= 1;
                                }
                                if (checkRandom == true) {
                                    Random random = new Random();
                                    int index = random.nextInt(baiHatList.size());
                                    if (index == position) {
                                        position = index - 1;
                                    }
                                    position = index;
                                }
                                if (position > (baiHatList.size()) - 1) {
                                    position = 0;
                                }
                                new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
                                mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                                getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
                                TimeSong();
                                UpdateTime();
                            }
                        }
                    });

                    handler.postDelayed(this, 500);
                }
            }
        }, 100);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true) {
                    if (position < (baiHatList.size())) {
                        binding.btnPlay.setImageResource(R.drawable.iconpause);
                        position++;
                        if (repeat == true) {
                            if (position == 0) {
                                position = baiHatList.size();
                            }
                            position -= 1;
                        }
                        if (checkRandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(baiHatList.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (baiHatList.size())) {
                            position = 0;
                        }
                        new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
                        mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                        getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
                    }

                    binding.btnBack.setClickable(false);
                    binding.btnBack.setClickable(false);
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnBack.setClickable(true);
                            binding.btnNext.setClickable(true);
                        }
                    }, 5000);
                    next = false;
                    handler1.removeCallbacks(this);

                } else {

                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    class PlayMusic extends AsyncTask<String, Void, String> {
        // khi thực hiện
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        // trả kết quả
        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // play dưới dạng online
                mMediaPlayer.setOnCompletionListener(mp -> {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                });
                mMediaPlayer.setDataSource(baihat);
                // khi media muốn phát đc
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    TimeSong();
                    UpdateTime();
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
