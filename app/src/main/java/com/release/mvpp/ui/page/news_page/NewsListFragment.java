package com.release.mvpp.ui.page.news_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.release.base.base.BaseMvpFragment;
import com.release.base.utils.ImageLoader;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.news.NewsListContract;
import com.release.mvpp.mvp.model.NewsInfoBean;
import com.release.mvpp.mvp.presenter.news.NewsListPresenter;
import com.release.mvpp.ui.adapter.NewsListAdapter;
import com.release.mvpp.ui.adapter.item.NewsMultiItem;
import com.release.mvpp.utils.NewsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

import static com.release.base.constance.BConstants.NEWS_TYPE_KEY;
import static com.release.base.constance.BConstants.NEWS_TYPE_TITLE;
import static com.release.mvpp.utils.Constants.NEWS_TOP_TYPE_ID;
import static com.release.mvpp.utils.Constants.PAGE;

/**
 * 要闻
 *
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class NewsListFragment extends BaseMvpFragment<NewsListContract.View, NewsListContract.Presenter> implements NewsListContract.View {

    @BindView(R.id.rv_news_list)
    RecyclerView rvNewsList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    BGABanner mBanner;
    NewsListAdapter mAdapter;
    private List<NewsMultiItem> mAdData = new ArrayList<>();
    private List<String> bannerImagedList = new ArrayList<>();
    private List<String> bannerTitleList = new ArrayList<>();
    private String mNewsId, mTitle;
    private boolean isFirst;

    public static NewsListFragment newInstance(String newsId, String title) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, newsId);
        bundle.putString(NEWS_TYPE_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected NewsListContract.Presenter createPresenter() {
        return new NewsListPresenter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        mTitle = getArguments().getString(NEWS_TYPE_TITLE);
        rvNewsList.setHasFixedSize(true);//让RecyclerView避免重新计算大小
        rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new NewsListAdapter(null, mTitle);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        rvNewsList.setAdapter(mAdapter);

        if (mNewsId.equals(NEWS_TOP_TYPE_ID))
            initBanner();
    }

    @Override
    public void startNet() {
        mPresenter.requestData(mNewsId, 0, false, true);
    }

    private void initBanner() {
        View bannerView = LayoutInflater.from(mContext).inflate(R.layout.item_newslist_banner, null);
        mAdapter.addHeaderView(bannerView);
        mBanner = bannerView.findViewById(R.id.banner);
        mBanner.setDelegate(bannerDelegate);
        mBanner.setAdapter(bannerAdapter);
    }

    private BGABanner.Adapter<ImageView, String> bannerAdapter = new BGABanner.Adapter<ImageView, String>() {
        @Override
        public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
            ImageLoader.loadCenterCrop(mContext, model, itemView, R.mipmap.placeholder_banner);
        }
    };

    /**
     * BannerClickListener
     */
    private BGABanner.Delegate bannerDelegate = new BGABanner.Delegate() {
        @Override
        public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
            NewsMultiItem item = mAdData.get(position);
            NewsInfoBean itemNewsBean = item.getNewsBean();
            switch (item.getItemType()) {
                case NewsMultiItem.ITEM_TYPE_NORMAL:
                    if (NewsUtils.isNewsSpecial(itemNewsBean.getSkipType()))
                        NewsSpecialActivity.start(mContext, itemNewsBean.getSpecialID(), itemNewsBean.getTitle());
                    else
                        NewsDetailActivity.start(mContext, itemNewsBean.getPostid(), itemNewsBean.getTitle());
                    break;
                case NewsMultiItem.ITEM_TYPE_PHOTO_SET:
                    PhotoAlbumActivity.start(mContext, itemNewsBean.getPhotosetID());
                    break;
            }
        }
    };


    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                int page = mAdapter.getData().size() / PAGE;
                mPresenter.requestData(mNewsId, page, false, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                mPresenter.requestData(mNewsId, 0, true, false);
            }
        });

    }


    @Override
    public void loadAdData(NewsInfoBean newsInfoBean) {

    }

    @Override
    public void loadData(Object object, boolean isRefresh) {
        List<NewsMultiItem> newsMultiItems = (List<NewsMultiItem>) object;
        if (isRefresh || !isFirst)
            initBannerData(newsMultiItems);
        isFirst = true;
        if (isRefresh)
            mAdapter.setList(newsMultiItems);
        else
            mAdapter.addData(newsMultiItems);
    }

    private void initBannerData(List<NewsMultiItem> newsMultiItems) {
        if (mNewsId.equals(NEWS_TOP_TYPE_ID)) {
            mAdData.clear();
            bannerTitleList.clear();
            bannerImagedList.clear();
            for (int i = 10; i < 15; i++) {
                NewsInfoBean newsBean = newsMultiItems.get(i).getNewsBean();
                bannerTitleList.add(newsBean.getTitle());
                mAdData.add(newsMultiItems.get(i));
                bannerImagedList.add(newsBean.getImgsrc());
            }
            mBanner.setData(bannerImagedList, bannerTitleList);
        }
    }
}
