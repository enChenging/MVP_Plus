package com.release.mvpp.mvp.presenter;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;

import com.release.base.base.BasePresenter;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvpp.mvp.contract.WebDetailContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */
public class WebDetailActivityPresenter extends BasePresenter<WebDetailContract.View> implements WebDetailContract.Presenter {


    @SuppressLint("CheckResult")
    public void loadFinish() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtil.bindLifecycle((LifecycleOwner) mView))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void requestData() {

    }
}
