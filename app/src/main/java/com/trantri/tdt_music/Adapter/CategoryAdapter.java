package com.trantri.tdt_music.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trantri.tdt_music.Model.Category;
import com.trantri.tdt_music.databinding.ItemCategoryBinding;

import java.util.List;

/**
 * Created by TranTien
 * Date 07/01/2020.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> mCategories;
    private DanhSachAllChuDeAdapter.OnItemClickedListener mListener;

    public CategoryAdapter(List<Category> mCategories, @NonNull DanhSachAllChuDeAdapter.OnItemClickedListener mListener) {
        this.mCategories = mCategories;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return (mCategories != null ? mCategories.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public ViewHolder(@NonNull ItemCategoryBinding itemUserBinding) {
            super(itemUserBinding.getRoot());

            binding = itemUserBinding;

            itemView.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
        }

        public void bindData(Category category) {
            Glide
                    .with(itemView.getContext())
                    .load(category.getIconId())
                    .into(binding.imgIcon);

            binding.tvNameDetail.setText(category.getName());
        }
    }
}
