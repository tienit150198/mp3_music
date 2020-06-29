package com.trantri.tdt_music.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.trantri.tdt_music.activity.PlayMusicActivity;
import com.trantri.tdt_music.data.local.PlayListUser;
import com.trantri.tdt_music.databinding.ItemSubPlaylistBinding;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
public class PlaylistUserAdapter extends RecyclerView.Adapter<PlaylistUserAdapter.PlaylistViewHolder> {
    private List<PlayListUser> mPlayListUsers;

    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public PlaylistUserAdapter(@NonNull List<PlayListUser> mPlayListUsers, @NonNull DanhSachAllChuDeAdapter.OnItemClickedListener listener) {
        this.mPlayListUsers = mPlayListUsers;
        mListener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaylistViewHolder(ItemSubPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.binData(mPlayListUsers.get(position));
    }

    private static final String TAG = "121212";

    @Override
    public int getItemCount() {
        return (mPlayListUsers != null ? mPlayListUsers.size() : 0);
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private ItemSubPlaylistBinding binding;

        public PlaylistViewHolder(@NonNull ItemSubPlaylistBinding itemSubPlaylistBinding) {
            super(itemSubPlaylistBinding.getRoot());
            binding = itemSubPlaylistBinding;


            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(getAdapterPosition());
                Log.d(TAG, "PlaylistViewHolder: " + getAdapterPosition());
            });

            binding.recyclerSubPlaylist.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerSubPlaylist.setHasFixedSize(true);
        }

        public void binData(PlayListUser playListUser) {
            binding.tvNamePlaylist.setText(playListUser.getName());
            SubPlaylistAdapter subPlaylistAdapter = new SubPlaylistAdapter(playListUser.getListBaiHatYeuThich(), new DanhSachAllChuDeAdapter.OnItemClickedListener() {
                @Override
                public void onItemClicked(int position) {
                    Log.d(TAG, "onItemClicked: " + mPlayListUsers.get(getAdapterPosition()).getListBaiHatYeuThich().get(position));
                    Intent intent = new Intent(itemView.getContext(), PlayMusicActivity.class);
                    intent.putExtra("cakhuc", new Gson().toJson(mPlayListUsers.get(getAdapterPosition()).getListBaiHatYeuThich().get(position)));
                    itemView.getContext().startActivity(intent);
                }
            });
            binding.recyclerSubPlaylist.setAdapter(subPlaylistAdapter);
        }

    }
}
