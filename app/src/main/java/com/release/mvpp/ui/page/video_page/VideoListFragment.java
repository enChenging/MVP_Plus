package com.release.mvpp.ui.page.video_page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.base.base.BaseMvpFragment;
import com.release.mvpp.R;
import com.release.mvpp.dao.VideoInfo;
import com.release.mvpp.mvp.contract.VideoListContract;
import com.release.mvpp.mvp.presenter.VideoListPrsenter;
import com.release.mvpp.ui.adapter.VideoListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import cn.jzvd.Jzvd;

import static com.release.base.constance.BConstants.VIDEO_ID_KEY;
import static com.release.mvpp.utils.Constants.PAGE;


/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class VideoListFragment extends BaseMvpFragment<VideoListContract.View, VideoListContract.Presenter> implements VideoListContract.View {

    private static final String TAG = VideoListFragment.class.getSimpleName();
    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    VideoListAdapter mAdapter;
    public String mVideoId;

    public static VideoListFragment newInstance(String videoId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID_KEY, videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_list;
    }

    @Override
    protected VideoListContract.Presenter createPresenter() {
        return new VideoListPrsenter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mVideoId = getArguments().getString(VIDEO_ID_KEY);

        mRvPhotoList.setHasFixedSize(true);
        mRvPhotoList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new VideoListAdapter(R.layout.adapter_video_list, null);
        mRvPhotoList.setAdapter(mAdapter);
    }

    @Override
    public void startNet() {
        mPresenter.requestData(mVideoId, 0, false, true);
    }

    @Override
    public void initListener() {
        mRvPhotoList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                        Jzvd.resetAllVideos();
                    }
                }
            }
        });

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishLoadMore(1000);
                int page = mAdapter.getData().size() / PAGE;
                mPresenter.requestData(mVideoId, page, false, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(1000);
                mPresenter.requestData(mVideoId, 0, true, false);
            }
        });
    }

    @Override
    public void loadData(Object data, boolean isRefresh) {
        List<VideoInfo> videoInfos = (List<VideoInfo>) data;
        if (isRefresh) {
            mAdapter.setNewData(videoInfos);
        } else {
            mAdapter.addData(videoInfos);
        }
    }
}
