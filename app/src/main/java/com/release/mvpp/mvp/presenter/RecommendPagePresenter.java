package com.release.mvpp.mvp.presenter;

import android.annotation.SuppressLint;

import com.release.base.base.BasePresenter;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.http.RetrofitHelper;
import com.release.mvpp.mvp.contract.RecommendPageContract;
import com.release.mvpp.mvp.model.RecommendPageBean;

import io.reactivex.Flowable;

/**
 * @author Mr.release
 * @create 2019/4/1
 * @Describe
 */
public class RecommendPagePresenter extends BasePresenter<RecommendPageContract.View> implements RecommendPageContract.Presenter {


    @SuppressLint("CheckResult")
    @Override
    public void requestData(boolean isRefresh) {
        Flowable<RecommendPageBean> flowable = RetrofitHelper.getRecommendData("4a0090627cf07a50def18da875165740", 20);
        HttpUtils.ext(flowable, mView, !isRefresh);

    }
}
