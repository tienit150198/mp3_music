package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.trantri.tdt_music.Adapter.DanhSachAllChuDeAdapter;
import com.trantri.tdt_music.Adapter.MusicLikedAdapter;
import com.trantri.tdt_music.Adapter.PlaylistUserAdapter;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.databinding.FragmentUserBinding;

import java.util.Objects;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;

    private AppDatabase mInstanceDatabase;

    private PlaylistUserAdapter mPlaylistUserAdapter;
    private MusicLikedAdapter mMusicLikedAdapter;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static final String TAG = "121212";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mInstanceDatabase = AppDatabase.getInstance(getContext());

        mInstanceDatabase.mPlayListUserDao().getAllPlaylist().observe(Objects.requireNonNull(getActivity()), playListUsers -> {
            if (playListUsers != null) {
                mPlaylistUserAdapter = new PlaylistUserAdapter(playListUsers, new DanhSachAllChuDeAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                        intent.putExtra("allbaihat", new Gson().toJson(playListUsers.get(position).getListBaiHatYeuThich()));
                        Objects.requireNonNull(getContext()).startActivity(intent);
                        Log.d(TAG, "onItemClicked: " + playListUsers.get(position));
                    }
                });
                binding.recyclerPlaylist.setAdapter(mPlaylistUserAdapter);
            }
        });

        mInstanceDatabase.mBaihatBaiHatYeuThichDao().getAllBaiHatYeuThich().observe(getActivity(), baiHatYeuThiches -> {
            if (baiHatYeuThiches != null) {
                mMusicLikedAdapter = new MusicLikedAdapter(Objects.requireNonNull(getContext()), baiHatYeuThiches, new DanhSachAllChuDeAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(int position) {
                        Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                        intent.putExtra("cakhuc", new Gson().toJson(baiHatYeuThiches.get(position)));
                        Objects.requireNonNull(getContext()).startActivity(intent);
                    }
                });
                binding.recyclerMusicLike.setAdapter(mMusicLikedAdapter);
            }
        });
        config();
    }

    private void config() {
        binding.recyclerPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPlaylist.setHasFixedSize(true);

        binding.recyclerMusicLike.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerMusicLike.setHasFixedSize(true);
    }
}