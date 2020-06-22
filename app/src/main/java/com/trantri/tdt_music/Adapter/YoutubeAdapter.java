package com.trantri.tdt_music.Adapter;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Model.youtube.Item;
import com.trantri.tdt_music.databinding.ItemMvBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranTien
 * Date 06/20/2020.
 */
public class YoutubeAdapter extends ListAdapter<Item, YoutubeAdapter.YoutubeViewHolder> {

    private static final DiffUtil.ItemCallback<Item> DIFF_ITEM = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId().getVideoId().equals(newItem.getId().getVideoId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getSnippet().getTitle().equals(newItem.getSnippet().getTitle());
        }
    };

    private OnItemClickListener mListener;
    private static final String TAG = "LOG_YoutubeAdapter";

    public YoutubeAdapter(@NonNull OnItemClickListener listener) {
        super(DIFF_ITEM);
        mListener = listener;
        Log.d(TAG, "YoutubeAdapter: Success");
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YoutubeViewHolder(ItemMvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    public class YoutubeViewHolder extends RecyclerView.ViewHolder {
        private ItemMvBinding binding;

        public YoutubeViewHolder(@NonNull ItemMvBinding itemMvBinding) {
            super(itemMvBinding.getRoot());

            binding = itemMvBinding;

            itemView.setOnClickListener(v -> mListener.onItemClicked(getItem(getAdapterPosition()).getId().getVideoId()));
        }

        public void bindData(Item item) {
            String url = item.getSnippet().getThumbnails().getMedium().getUrl();
            String name = item.getSnippet().getTitle();
            Log.d(TAG, "bindData: " + name);

            Glide.with(binding.getRoot()
                    .getContext())
                    .load(url)
                    .into(binding.imgThumbnail);

            if(Build.VERSION.SDK_INT >= 24){
                binding.tvNameMv.setText(Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY));
            }else{
                binding.tvNameMv.setText(Html.fromHtml(name));
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClicked(String videoId);
    }
}
