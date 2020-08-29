package com.release.mvpp.ui.page.video_page;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.release.base.base.BaseMvpFragment;
import com.release.base.base.ViewPagerAdapter;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.VideoPageContract;
import com.release.mvpp.mvp.presenter.VideoPagePresenter;

import java.util.ArrayList;
import butterknife.BindView;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class VideoPage extends BaseMvpFragment<VideoPageContract.View,VideoPageContract.Presenter> implements VideoPageContract.View {


    @BindView(R.id.stl_tab_layout)
    SlidingTabLayout mStlTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    ViewPagerAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.page_video;
    }


    @Override
    protected VideoPageContract.Presenter createPresenter() {
        return new VideoPagePresenter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mAdapter = new ViewPagerAdapter(getFragmentManager());
    }


    @Override
    public void startNet() {
        mPresenter.requestData();
    }

    @Override
    public void loadData(ArrayList<Fragment> fragments, String[] strings) {
        mAdapter.setItems(fragments, strings);
        mViewPager.setAdapter(mAdapter);
        mStlTabLayout.setViewPager(mViewPager);
    }
}
