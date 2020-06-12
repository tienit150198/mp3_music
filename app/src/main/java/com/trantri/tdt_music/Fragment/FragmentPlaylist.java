package com.trantri.tdt_music.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trantri.tdt_music.activity.PlaylistActivity;
import com.trantri.tdt_music.activity.SongsListActivity;
import com.trantri.tdt_music.Adapter.PlaylistAdapter;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPlaylist extends Fragment {
    View view;
    ListView mListViewPlaylist;
    TextView txtTiltlePlaylist, txtPlaylistGanDay;
    PlaylistAdapter mPlaylistAdapter;
    List<Playlist> mList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        intitView();
        GetData();
        ActionView();
        return view;
    }

    private void ActionView() {
    txtPlaylistGanDay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), PlaylistActivity.class);
            startActivity(intent);
        }
    });
    }

    private void intitView() {
        mListViewPlaylist = view.findViewById(R.id.lv_playlist);
        txtTiltlePlaylist = view.findViewById(R.id.tv_titlePlaylist);
        txtPlaylistGanDay = view.findViewById(R.id.tv_morePlaylist);
    }

    private void GetData() {
        DataService mDataService = APIService.getService();

        compositeDisposable.add(
                mDataService.getDataPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((playlists, throwable) -> {
                    if(throwable != null){
                        // error
                    }else{
                        mList = playlists;
                        mPlaylistAdapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1, mList);
                        mListViewPlaylist.setAdapter(mPlaylistAdapter);
                        setListViewHeightBasedOnChildren(mListViewPlaylist);
                        mListViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), SongsListActivity.class);
                                intent.putExtra("itemPlaylist", mList.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                })

        );


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
