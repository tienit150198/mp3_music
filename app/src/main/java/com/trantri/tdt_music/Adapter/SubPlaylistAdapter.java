package com.trantri.tdt_music.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.SubPlaylist;
import com.trantri.tdt_music.databinding.ItemAlbumBinding;
import com.trantri.tdt_music.databinding.ItemSubPlaylistBinding;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
public class SubPlaylistAdapter extends RecyclerView.Adapter<SubPlaylistAdapter.SubPlayListViewHolder> {
    private List<SubPlaylist> mSubPlaylists;

    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public SubPlaylistAdapter(List<SubPlaylist> subPlaylists, @NonNull DanhSachAllChuDeAdapter.OnItemClickedListener listener) {
        mSubPlaylists = subPlaylists;
        mListener = listener;
    }

    @NonNull
    @Override
    public SubPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubPlayListViewHolder(ItemSubPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlayListViewHolder holder, int position) {
        holder.bindData(mSubPlaylists.get(position));
    }

    private static final String TAG = "121212";

    @Override
    public int getItemCount() {
        return (mSubPlaylists != null ? mSubPlaylists.size() : 0);
    }

    public class SubPlayListViewHolder extends RecyclerView.ViewHolder {
        private ItemSubPlaylistBinding binding;

        public SubPlayListViewHolder(@NonNull ItemSubPlaylistBinding itemAlbumBinding) {
            super(itemAlbumBinding.getRoot());
            binding = itemAlbumBinding;

            binding.recyclerSubPlaylist.setHasFixedSize(true);
            binding.recyclerSubPlaylist.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void bindData(SubPlaylist subPlaylist) {
            binding.tvNamePlaylist.setText(subPlaylist.getName());

            CategoryAdapter categoryAdapter = new CategoryAdapter(subPlaylist.getCategory(), new DanhSachAllChuDeAdapter.OnItemClickedListener() {
                @Override
                public void onItemClicked(int position) {
                    mListener.onItemClicked(getAdapterPosition(), position);
                }
            });

            binding.recyclerSubPlaylist.setAdapter(categoryAdapter);
        }
    }
}
