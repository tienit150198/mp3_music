package com.trantri.tdt_music.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trantri.tdt_music.Adapter.QuangCaoAdapter;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.data.remote.ApiClient;
import com.trantri.tdt_music.databinding.FragmentQuangcaoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_QuangCao extends Fragment {
    private QuangCaoAdapter mAdapter;
    private Runnable mRunnable;
    private Handler mHandler;
    private int item;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentQuangcaoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuangcaoBinding.inflate(inflater, container, false);
        GetBanner();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static final String TAG = "LOG_Fragment_QuangCao";

    private void GetBanner() {
        ApiClient.getService(Objects.requireNonNull(getContext()))
                .getDataBanner()
                .enqueue(new Callback<List<Quangcao>>() {
                    @Override
                    public void onResponse(Call<List<Quangcao>> call, Response<List<Quangcao>> response) {
                        if (response.isSuccessful()) {
                            final List<Quangcao> arrayListBanner = response.body();
                            mAdapter = new QuangCaoAdapter(getActivity(), (ArrayList<Quangcao>) arrayListBanner);
                            binding.viewPager.setAdapter(mAdapter);
                            // hiện ra số lượng indicator tùy theo số lượng pager
                            binding.myIndicator.setViewPager(binding.viewPager);
                            mHandler = new Handler();
                            // thực hiện hành động khi handler gọi
                            mRunnable = () -> {
                                // item hiện tại đang đứng ở đâu
                                item = binding.viewPager.getCurrentItem();
                                item++;
                                // nếu  vượt quá kích thức page thì trở lại pager đầu
                                assert arrayListBanner != null;
                                if (item >= arrayListBanner.size()) {
                                    item = 0;
                                }
                                // chạy xong set dữ liệu lên
                                binding.viewPager.setCurrentItem(item, true);
                                //mHandler.postDelayed(mRunnable, 4000);
                            };
                            mHandler.postDelayed(mRunnable, 4000);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Quangcao>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
