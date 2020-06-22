package com.trantri.tdt_music.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ConfigRetrofitClient {
    private static Retrofit retrofit = null;

    // Cấu hình retrofit
    public static Retrofit getClient(String url_base){
        // những giao thức mạng tương tác phía server
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS) // ngắt thời gian mà chúng ta đọc
                 .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000 , TimeUnit.MILLISECONDS) // đợi lâu ngắt kết nối
                .retryOnConnectionFailure(true)   // cố gắng két nối
                .protocols(Collections.singletonList(Protocol.HTTP_1_1)) // khi kết nối phần giao thức server ko tìm đc như : HTTP..
                .build();

        // khi dữ liệu trả từ server về Gson sẽ thực hiện đọc dữ liệu từ khóa API về thành từ khóa java
        Gson mGson = new GsonBuilder().setLenient().create();
        // gắn vào retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(url_base)
                .client(mOkHttpClient) // cấu hình tương tác của mảng
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson)) // convert dữ liệu API thành dữ liệu java
                .build();
    return retrofit;
    }
}
