package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trantri.tdt_music.activity.PlayVideoActivity;
import com.trantri.tdt_music.Adapter.VideoAdapter;
import com.trantri.tdt_music.Model.MyVideo;
import com.trantri.tdt_music.R;

import java.util.Vector;

public class FragmentMV extends Fragment {
    View view;
    TextView txtMV, txtKara;
    RecyclerView mRecyclerViewVideoMV,mRecyclerViewKaraoke;
    Vector<MyVideo> myVideos = new Vector<MyVideo>();
    Vector<MyVideo> myVideosKaraoke = new Vector<MyVideo>();
    VideoAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mv, container, false);
        txtMV = view.findViewById(R.id.tv_mv);

        txtMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }




}
