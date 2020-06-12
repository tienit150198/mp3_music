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
import androidx.viewpager.widget.ViewPager;

import com.trantri.tdt_music.Adapter.QuangCaoAdapter;
import com.trantri.tdt_music.Model.Quangcao;
import com.trantri.tdt_music.R;
import com.trantri.tdt_music.Service.APIService;
import com.trantri.tdt_music.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

public class Fragment_QuangCao extends Fragment {
    View view;
    ViewPager mViewPager;
    QuangCaoAdapter mAdapter;
    CircleIndicator mCircleIndicator;
    Runnable mRunnable;
    Handler mHandler;
    int item;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quangcao, container, false);
        initView();
        GetBanner();
        return view;
    }

    public void initView() {
        mViewPager = view.findViewById(R.id.viewPager);
        mCircleIndicator = view.findViewById(R.id.myIndicator);

    }

    private static final String TAG = "LOG_Fragment_QuangCao";

    public void GetBanner() {
        DataService mDataService = APIService.getService(); // khởi tạo retrofit để đẩy lên


        compositeDisposable.add(
                mDataService.getDataBanner()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((quangcaos, throwable) -> {
                            if (throwable != null) {
                                Log.d(TAG, "GetBanner: " + throwable.getMessage());
                            } else {
                                final List<Quangcao> arrayListBanner = quangcaos;
                                mAdapter = new QuangCaoAdapter(getActivity(), (ArrayList<Quangcao>) arrayListBanner);
                                mViewPager.setAdapter(mAdapter);
                                // hiện ra số lượng indicator tùy theo số lượng pager
                                mCircleIndicator.setViewPager(mViewPager);
                                mHandler = new Handler();
                                // thực hiện hành động khi handler gọi
                                mRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        // item hiện tại đang đứng ở đâu
                                        item = mViewPager.getCurrentItem();
                                        item++;
                                        // nếu  vượt quá kích thức page thì trở lại pager đầu
                                        assert arrayListBanner != null;
                                        if (item >= arrayListBanner.size()) {
                                            item = 0;
                                        }
                                        // chạy xong set dữ liệu lên
                                        mViewPager.setCurrentItem(item, true);
                                        //mHandler.postDelayed(mRunnable, 4000);
                                    }

                                };
                                mHandler.postDelayed(mRunnable, 4000);
                            }
                        })


        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
