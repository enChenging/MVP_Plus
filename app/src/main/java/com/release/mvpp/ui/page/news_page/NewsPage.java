package com.release.mvpp.ui.page.news_page;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.release.base.base.BaseMvpFragment;
import com.release.base.base.ViewPagerAdapter;
import com.release.mvpp.MainActivity;
import com.release.mvpp.R;
import com.release.mvpp.dao.NewsTypeInfo;
import com.release.mvpp.mvp.contract.news.NewsPageContract;
import com.release.mvpp.mvp.presenter.news.NewsPagePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class NewsPage extends BaseMvpFragment<NewsPageContract.View, NewsPageContract.Presenter> implements NewsPageContract.View {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    @BindView(R.id.stl_tab_layout)
    SlidingTabLayout mStlTabLayout;

    ViewPagerAdapter mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.page_news;
    }

    @Override
    protected NewsPageContract.Presenter createPresenter() {
        return new NewsPagePresenter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mAdapter = new ViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_setting, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                ((MainActivity) getActivity()).toggle();
                break;
            case R.id.iv_search:
                Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void startNet() {

        mPresenter.requestData();
    }

    @Override
    public void loadData(List<NewsTypeInfo> newsTypeInfos) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        for (NewsTypeInfo bean : newsTypeInfos) {
            titles.add(bean.getName());
            fragments.add(NewsListFragment.newInstance(bean.getTypeId(),bean.getName()));
        }

        mAdapter.setItems(fragments, titles);
        mStlTabLayout.setViewPager(mViewPager);
    }
}
