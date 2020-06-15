package com.trantri.tdt_music.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.trantri.tdt_music.Adapter.QuangCaoAdapter;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.ApiClient;
import com.trantri.tdt_music.Service.DataService;
import com.trantri.tdt_music.databinding.FragmentQuangcaoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

public class Fragment_QuangCao extends Fragment {
    private QuangCaoAdapter mAdapter;
    private Runnable mRunnable;
    private Handler mHandler;
    private int item;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    FragmentQuangcaoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuangcaoBinding.inflate(getLayoutInflater());
        GetBanner();
        return binding.getRoot();
    }

    private static final String TAG = "LOG_Fragment_QuangCao";

    private void GetBanner() {
        compositeDisposable.add(
                ApiClient.getService(Objects.requireNonNull(getContext()))
                        .getDataBanner()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((quangcaoList) -> {
                                final List<Quangcao> arrayListBanner = quangcaoList;
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

                        })
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
