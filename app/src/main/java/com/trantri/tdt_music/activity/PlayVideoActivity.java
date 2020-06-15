package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.trantri.tdt_music.Adapter.VideoYoutubeAdapter;
import com.trantri.tdt_music.Model.YoutubeMusic;
import com.trantri.tdt_music.databinding.ActivityPlayVideoBinding;

import java.util.ArrayList;
import java.util.Objects;

public class PlayVideoActivity extends AppCompatActivity {

    protected static String API_KEY = "AIzaSyBE-8j4c1hviqwENsua7mKJAWSPsGNDPME";
    private String ID_PLAYLIST = "PLQDIMgoD-XFSrzVYJoilDK45ZAtugFe35";
    private String URL_GET_JSON = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + ID_PLAYLIST + "&key=" + API_KEY + "&maxResults=50";
    private ArrayList<YoutubeMusic> mListVideo;
    private VideoYoutubeAdapter mAdapterVideo;
    ActivityPlayVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
//        GetJsonYoutbue(URL_GET_JSON);
    }

    private void init() {
        setSupportActionBar(binding.toobarDanhSachPhat);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Danh Sách MV");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toobarDanhSachPhat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListVideo = new ArrayList<>();
        binding.lvVideo.hasFixedSize();
        binding.lvVideo.setLayoutManager(new GridLayoutManager(PlayVideoActivity.this, 2));
        mAdapterVideo = new VideoYoutubeAdapter(PlayVideoActivity.this, mListVideo, (view, position) -> {
            Intent intent = new Intent(PlayVideoActivity.this, MediaplayerVideoActivity.class);
            intent.putExtra("IdVideo", mListVideo.get(position).getmIdVideo());
            startActivity(intent);
        });
        binding.lvVideo.setAdapter(mAdapterVideo);

    }
//
//    public void GetJsonYoutbue(String url){
//        DataService dataService = APIService.getService();
//
//        dataService
//    }
//
//    public void GetJsonYoutbue(String url) {
//        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("items");
//                    String title = "";
//                    String link = "";
//                    String chanal = "";
//                    String idVideo = "";
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
//                        JSONObject jsonObjectSnippet = jsonObjectItem.getJSONObject("snippet");
//                        title = jsonObjectSnippet.getString("title");
//                        JSONObject jsonObjectThumbnail = jsonObjectSnippet.getJSONObject("thumbnails");
//                        JSONObject jsonObjectMedium = jsonObjectThumbnail.getJSONObject("medium");
//                        link = jsonObjectMedium.getString("url");
//                        chanal = jsonObjectSnippet.getString("channelTitle");
//                        JSONObject jsonResourceID = jsonObjectSnippet.getJSONObject("resourceId");
//                        idVideo = jsonResourceID.getString("videoId");
//                        mListVideo.add(new YoutubeMusic(title, idVideo, link, chanal));
//                    }
//                    mAdapterVideo.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//    }
}
