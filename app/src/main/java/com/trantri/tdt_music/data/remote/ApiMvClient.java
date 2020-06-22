package com.trantri.tdt_music.data.remote;

/**
 * Created by TranTien
 * Date 06/20/2020.
 */
public class ApiMvClient {
    // truyền url để tướng tác phía server
    private static String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    // tướng tác tích hợp 2 thằng lại
    public static DataService getService(){
        return ConfigRetrofitClient.getClient(BASE_URL).create(DataService.class);
    }
}
