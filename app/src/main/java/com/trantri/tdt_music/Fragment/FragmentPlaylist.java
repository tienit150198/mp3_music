package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.activity.PlaylistActivity;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Adapter.PlaylistAdapter;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.databinding.FragmentPlaylistBinding;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentPlaylist extends Fragment {
    PlaylistAdapter mPlaylistAdapter;
    List<Playlist> mList;
    FragmentPlaylistBinding binding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(getLayoutInflater());
        GetData();
        ActionView();
        return binding.getRoot();
    }

    private void ActionView() {
        binding.tvTitlePlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PlaylistActivity.class);
            startActivity(intent);
        });
    }

    private static final String TAG = "LOG_FragmentPlaylist";
    private void GetData() {
      Disposable disposable = ApiClient.getService(Objects.requireNonNull(getContext())).getDataPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((playlists, throwable) -> {
                    if(throwable != null){
                        Log.d(TAG, "Getdata: " + throwable.getMessage());
                        return;
                    }
                    mList = playlists;
                    mPlaylistAdapter = new PlaylistAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, mList);
                    binding.lvPlaylist.setAdapter(mPlaylistAdapter);
                    setListViewHeightBasedOnChildren(binding.lvPlaylist);
                    binding.lvPlaylist.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(getActivity(), SongsListActivity.class);
                        intent.putExtra("itemPlaylist", mList.get(position));
                        startActivity(intent);

                    });
                });
      compositeDisposable.add(disposable);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
