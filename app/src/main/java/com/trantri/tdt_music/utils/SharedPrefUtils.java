package com.trantri.tdt_music.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.trantri.tdt_music.App;

/**
 * Created by TranTien
 * Date 06/21/2020.
 */
public class SharedPrefUtils {
    private static final String PREFS_NAME = "share_prefs";

    private static SharedPrefUtils mInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPrefUtils() {
        mSharedPreferences = App.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPrefUtils getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefUtils();
        }
        return mInstance;
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, App.getInstance().getGson().toJson(data));
        }
        editor.apply();
    }

    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        } else {
            return App.getInstance().getGson().fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
        }
    }

    public <T> T get(String key, Class<T> anonymousClass, T defaultData) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, (String) defaultData);
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, (Boolean) defaultData));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, (Float) defaultData));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, (Integer) defaultData));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, (Long) defaultData));
        } else {
            return App.getInstance().getGson().fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
        }
    }
}
