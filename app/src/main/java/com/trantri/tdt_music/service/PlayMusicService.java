package com.trantri.tdt_music.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.MessageEventBus;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.data.Constraint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

/**
 * Created by TranTien
 * Date 06/21/2020.
 */
public class PlayMusicService extends Service {

    public static MediaPlayer mMediaPlayer;

    public static List<BaiHatYeuThich> mListBaiHat;
    public static int mCurrentPosition;
    public static BehaviorSubject<Integer> mCurrentTime = BehaviorSubject.createDefault(0);

    public static boolean isLoop = false;
    public static boolean isSuffix = false;
    public static Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable currentPositionListener = new Runnable() {
        @Override
        public void run() {
            mCurrentTime.onNext(mMediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mCurrentPosition = 0;
        mHandler = new Handler();
        mListBaiHat = new ArrayList<>();
        EventBus.getDefault().register(this);

        super.onCreate();
    }

    private static final String TAG = "xxxxxxxxxxxxxx SERVICE";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        getListMusic(intent);

        // start first music
        playMusic(0);

        return super.onStartCommand(intent, flags, startId);
    }

    public void getListMusic(Intent intent) {
        mListBaiHat.clear();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                String text = intent.getStringExtra("cakhuc");
                BaiHatYeuThich baiHatYeuThich = new Gson().fromJson(text, BaiHatYeuThich.class);
                mListBaiHat.add(baiHatYeuThich);
            }
            if (intent.hasExtra("allbaihat")) {
                String txtAllBaiHat = intent.getStringExtra("allbaihat");
                Type type = new TypeToken<List<BaiHatYeuThich>>() {
                }.getType();

                mListBaiHat = new Gson().fromJson(txtAllBaiHat, type);
            }

            Log.d(TAG, "GetDataFromItent: " + (mListBaiHat != null ? mListBaiHat.toString() : null));
        }
    }

    public static BehaviorSubject<Integer> getCurrentTime() {
        return mCurrentTime;
    }

    public void playMusic(int positionPlay) {
        if (mListBaiHat.size() > positionPlay && mListBaiHat.get(positionPlay) != null) {
            if(mMediaPlayer == null){
                mMediaPlayer = new MediaPlayer();
                try {
                    Log.d(TAG, "playMusic: " + Uri.parse(mListBaiHat.get(positionPlay).getLinkBaiHat()));
                    mCurrentPosition = positionPlay;

                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    mMediaPlayer.setDataSource(mListBaiHat.get(positionPlay).getLinkBaiHat());

                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnCompletionListener(mp -> {
                        if(isSuffix){
                            int nextPosition = new Random().nextInt(mListBaiHat.size());
                            if(nextPosition == positionPlay){
                                if(nextPosition == 0){
                                    nextPosition++;
                                }else{
                                    nextPosition--;
                                }
                            }

                            playMusic(nextPosition);
                        }else{
                            nextMusic();
                        }
                        Log.d(TAG, "playMusic: COMPLETED");
                        EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.COMPLETED, null));
                        mHandler.removeCallbacks(currentPositionListener);
                    });
                    mMediaPlayer.setOnPreparedListener(mp -> {
                        Log.d(TAG, "playMusic: PREPARED");
                        mp.start();
                        EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.PREPARED, new InformationMusic(mListBaiHat.get(positionPlay), mp.getDuration(), positionPlay)));
                        mHandler.post(currentPositionListener);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                cancel();
                playMusic(positionPlay);
            }

        } else {
            Log.d(TAG, "playMusic: not found in position");
        }
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mHandler.removeCallbacks(currentPositionListener);
        }
    }

    public void resumeMusic() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            mHandler.post(currentPositionListener);
        }
    }

    public void stopMusic() {
        mMediaPlayer.stop();
        mHandler.removeCallbacks(currentPositionListener);
    }

    public void changePosition(int timeMilis) {
        mMediaPlayer.seekTo(timeMilis);
        if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    public void nextMusic() {
        if (mCurrentPosition + 1 < mListBaiHat.size() && mListBaiHat.get(mCurrentPosition + 1) != null) {
            mCurrentPosition++;

            stopMusic();
            playMusic(mCurrentPosition);
        }else{
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.FAIL, "This is the last song"));
        }
    }

    public void previousMusic() {
        if (mCurrentPosition > 0 && mListBaiHat.size() > (mCurrentPosition - 1) && mListBaiHat.get(mCurrentPosition - 1) != null) {
            mCurrentPosition--;

            stopMusic();

            playMusic(mCurrentPosition);
        }else{
            EventBus.getDefault().post(new MessageEventBus(Constraint.EventBusAction.FAIL, "This is the first song"));
        }
    }

    private void cancel() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Subscribe
    public void onEvent(MessageEventBus messageEventBus) {
        switch (messageEventBus.message) {
            case Constraint
                    .EventBusAction.PLAY:

                int positionPlay = 0;
                if (messageEventBus.action != null) {
                    InformationMusic informationMusic = (InformationMusic) messageEventBus.action;
                    Log.d(TAG, "onEvent: " + messageEventBus.action.toString());
                    positionPlay = informationMusic.getPosition();
                }
                playMusic(positionPlay);
                break;
            case Constraint
                    .EventBusAction.PAUSE:
                pauseMusic();
                break;
            case Constraint.EventBusAction.RESUME:
                resumeMusic();
                break;
            case Constraint
                    .EventBusAction.PREVIOUS:
                Log.d(TAG, "onEvent: PREVIOUS");
                previousMusic();
                break;

            case Constraint
                    .EventBusAction.NEXT:
                Log.d(TAG, "onEvent: NEXT");
                nextMusic();
                break;
            case Constraint.EventBusAction.LOOP:
                if (messageEventBus.action != null) {
                    isLoop = (boolean) messageEventBus.action;
                }

                mMediaPlayer.setLooping(isLoop);
                break;
            case Constraint.EventBusAction.SUFFIX:
                if (messageEventBus.action != null) {
                    isSuffix = (boolean) messageEventBus.action;
                }
                break;
            case Constraint.EventBusAction.SEEK:
                int timeSeek = mMediaPlayer.getCurrentPosition();

                if (messageEventBus.action != null) {
                    timeSeek = (int) messageEventBus.action;
                }
                changePosition(timeSeek);
                break;

            case Constraint.EventBusAction.UPDATE:
                if(messageEventBus.action != null){
                    BaiHatYeuThich baiHatYeuThich = (BaiHatYeuThich) messageEventBus.action;

                    for(int i = 0 ; i < mListBaiHat.size() ; i++){
                        if(mListBaiHat.get(i).getTenBaiHat().equals(baiHatYeuThich.getTenBaiHat())){
                            mListBaiHat.set(i, baiHatYeuThich);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        cancel();
        EventBus.getDefault().unregister(this);
    }
}
