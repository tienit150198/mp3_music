package com.trantri.tdt_music.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Created by TranTien
 * Date 06/20/2020.
 */
public class NetworkUtils {
    // check wifi connected
    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(manager).getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
