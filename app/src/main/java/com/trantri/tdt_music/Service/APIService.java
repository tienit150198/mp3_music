package com.trantri.tdt_music.Service;

public class APIService {
    // truyền url để tướng tác phía server
    private static String url_base = "https://trandinhtri201096.000webhostapp.com/Server/";
    // tướng tác tích hợp 2 thằng lại
    public static DataService getService(){
        return ConfigRetrofitClient.getClient(url_base).create(DataService.class);
    }
}
