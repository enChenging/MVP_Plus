package com.release.mvpp.mvp.presenter;

import com.release.base.base.BasePresenter;
import com.release.mvpp.dao.VideoInfo;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.http.RetrofitHelper;
import com.release.mvpp.mvp.contract.VideoListContract;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class VideoListPrsenter extends BasePresenter<VideoListContract.View> implements VideoListContract.Presenter {


    @Override
    public void requestData(String videoId, int page, boolean isRefresh) {

        Flowable<List<VideoInfo>> flowable = RetrofitHelper.getVideoListAPI(videoId, page);
        HttpUtils.ext(flowable, mView, page == 0 && !isRefresh);

    }

}
