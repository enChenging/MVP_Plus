package com.release.mvpp.ui.page.recommend_page;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.release.base.base.BaseMvpFragment;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.RecommendPageContract;
import com.release.mvpp.mvp.model.RecommendPageBean;
import com.release.mvpp.mvp.presenter.RecommendPagePresenter;
import com.release.mvpp.ui.adapter.RecommendAdapter;
import com.release.mvpp.ui.web_detail.WebDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class RecommendPage extends BaseMvpFragment<RecommendPageContract.View, RecommendPageContract.Presenter> implements RecommendPageContract.View {
    private static final String TAG = RecommendPage.class.getSimpleName();


    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    RecommendAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.page_recommend;
    }

    @Override
    protected RecommendPageContract.Presenter createPresenter() {
        return new RecommendPagePresenter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mRvList.setHasFixedSize(true);
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RecommendAdapter(R.layout.item_recommend, null);
        mRvList.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

        mRefreshLayout.setEnableLoadMore(false);//关闭下拉加载
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.getLoadMoreModule().setEnableLoadMore(false);
                mPresenter.requestData(true);
                mRefreshLayout.finishRefresh(1000);
            }
        });


        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                RecommendPageBean.NewslistBean bean = (RecommendPageBean.NewslistBean) adapter.getData().get(position);
                WebDetailActivity.start(mContext, bean.getTitle(), bean.getCtime(), bean.getDescription(), bean.getUrl());
            }
        });
    }

    @Override
    public void startNet() {
        mPresenter.requestData(false);
    }


    @Override
    public void loadData(Object data) {
        RecommendPageBean bean = (RecommendPageBean) data;
        List<RecommendPageBean.NewslistBean> newslist = bean.getNewslist();
        mAdapter.setNewData(newslist);
    }


}
