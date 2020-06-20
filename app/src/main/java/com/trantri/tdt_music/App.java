package com.trantri.tdt_music;

import android.app.Application;
import android.telephony.SubscriptionManager;

import com.google.gson.Gson;

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
