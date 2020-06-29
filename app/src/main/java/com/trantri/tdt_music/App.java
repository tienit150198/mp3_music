package com.trantri.tdt_music;

import android.app.Application;
import android.telephony.SubscriptionManager;

import com.google.gson.Gson;
import com.trantri.tdt_music.Fragment.FragmentAlbum;
import com.trantri.tdt_music.Fragment.FragmentBaiHat;
import com.trantri.tdt_music.Fragment.FragmentChuDeThLoai;
import com.trantri.tdt_music.Fragment.FragmentPlaylist;
import com.trantri.tdt_music.Fragment.Fragment_QuangCao;
import com.trantri.tdt_music.activity.DanhSachAllAlbumActivity;

/**
 * Created by TranTien
 * Date 06/21/2020.
 */
public class App extends Application {
    private static App sInstance;
    private Gson mGson;

    public static synchronized App getInstance() {
        return sInstance;
    }

    public Gson getGson() {
        return mGson;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mGson = new Gson();
    }
}
