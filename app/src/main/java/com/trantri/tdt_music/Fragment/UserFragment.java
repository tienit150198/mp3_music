package com.trantri.tdt_music.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.trantri.tdt_music.Adapter.SubPlaylistAdapter;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.Category;
import com.trantri.tdt_music.Model.SubPlaylist;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.activity.DanhSachTheLoaiTheoChuDeActivity;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.data.local.AppDatabase;
import com.trantri.tdt_music.data.local.PlayListUser;
import com.trantri.tdt_music.databinding.FragmentUserBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFragment extends Fragment implements DanhSachAllChuDeAdapter.OnItemClickedListener {
    private FragmentUserBinding binding;

    private AppDatabase mInstaceDatabase;
    private SubPlaylistAdapter mSubPlaylistAdapter;
    private List<SubPlaylist> mSubPlaylists = new ArrayList<>();

    private List<BaiHatYeuThich> mBaiHatDaThiches = new ArrayList<>();
    private List<PlayListUser> mPlayListUsers = new ArrayList<>();

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
        mInstaceDatabase = AppDatabase.getInstance(getContext());

        mInstaceDatabase.mBaihatBaiHatYeuThichDao()
                .getAllBaiHatYeuThich()
                .observe(Objects.requireNonNull(getActivity()), baiHatYeuThiches -> {
                    if (baiHatYeuThiches != null) {
                        mBaiHatDaThiches = baiHatYeuThiches;
                    }
                });

        mInstaceDatabase.mPlayListUserDao()
                .getAllPlaylist()
                .observe(getActivity(), playListUsers -> {
                    if (playListUsers != null) {
                        mPlayListUsers = playListUsers;
                    }
                });

        initData();
        config();
    }

    private void initData() {
        SubPlaylist subPlaylist1 = new SubPlaylist();
        SubPlaylist subPlaylist2 = new SubPlaylist();

        List<Category> categories1 = new ArrayList<>();
        List<Category> categories2 = new ArrayList<>();

        subPlaylist1.setName("Nhạc Của Tui");
        subPlaylist2.setName("Ứng Dụng");

        categories1.add(new Category(R.drawable.ic_playlist, "Playlist"));
        categories1.add(new Category(R.drawable.ic_favorite, "Bài hát đã thích"));
        categories2.add(new Category(R.drawable.ic_share, "Chia sẻ"));
        categories2.add(new Category(R.drawable.ic_rate, "Đánh giá ứng dụng"));

        subPlaylist1.setCategory(categories1);
        subPlaylist2.setCategory(categories2);

        mSubPlaylists.add(subPlaylist1);
        mSubPlaylists.add(subPlaylist2);
    }

    private void config() {
        Log.d(TAG, "config: " + mSubPlaylists.toString());
        mSubPlaylistAdapter = new SubPlaylistAdapter(mSubPlaylists, this);

        binding.recyclerUser.setAdapter(mSubPlaylistAdapter);
        binding.recyclerUser.setHasFixedSize(true);
        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClicked(int parentPosition, int subPosition) {
        if (parentPosition == 0) {
            if (subPosition == 0) {
                // show all playlist same chude
                Intent intent = new Intent(getContext(), DanhSachTheLoaiTheoChuDeActivity.class);
                intent.putExtra(Constraint.Intent.USER_PLAYLIST, new Gson().toJson(mPlayListUsers));

                startActivity(intent);
            } else {
                // music liked
                Intent intent = new Intent(getContext(), SongsListActivity.class);
                intent.putExtra(Constraint.Intent.USER, new Gson().toJson(mBaiHatDaThiches));
                startActivity(intent);
            }
        } else {
            if (subPosition == 0) {
                shareApp();
            } else {
                rateApp();
            }
        }
    }

    private void rateApp() {
        Uri uri = Uri.parse("http://play.google.com/store/apps");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }

        goToMarket.addFlags(flags);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps")));
        }
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps");
        sendIntent.setType("text/plain");

        Intent intentShare = Intent.createChooser(sendIntent, null);
        startActivity(intentShare);
    }
}