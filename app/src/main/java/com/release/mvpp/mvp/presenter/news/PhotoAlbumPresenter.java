package com.release.mvpp.mvp.presenter.news;

import android.annotation.SuppressLint;

import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.mvp.contract.news.PhotoAlbumContract;
import com.release.mvpp.mvp.model.PhotoSetInfoBean;
import com.release.mvpp.http.RetrofitHelper;

import io.reactivex.Flowable;

/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class PhotoAlbumPresenter extends BasePresenter<PhotoAlbumContract.View> implements PhotoAlbumContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void requestData(String photoSetId) {
        Logger.i("loadData---mPhotoSetId: " + photoSetId);
        Flowable<PhotoSetInfoBean> flowable = RetrofitHelper.getPhotoAlbumAPI(photoSetId);
        HttpUtils.ext(flowable,mView,true);
    }
}
