package com.release.mvpp.http;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;

import com.orhanobut.logger.Logger;
import com.release.base.base.IView;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.mvpp.App;
import com.release.mvpp.R;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public class HttpUtils {

    @SuppressLint("CheckResult")
    public static <T> void ext(Flowable<T> flowable, IView view, boolean isRefresh,boolean isShowLoading) {

        flowable.compose(RxUtil.rxSchedulerHelper())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if (isShowLoading) view.showLoading();
                    }
                })
                .as(RxUtil.bindLifecycle((LifecycleOwner) view))
                .subscribeWith(new CommonSubscriber<T>() {
                    @Override
                    protected void _onNext(T bean) {
//                        Logger.d("_onNext: " + bean);
                        view.loadData(bean,isRefresh);
                    }

                    @Override
                    protected void _onError(String message) {
                        Logger.d("_onError: " + message);
                        if (isShowLoading)
                            view.showError();
                        else
                            view.showError(App.getInstance().getString(R.string.data_error));
                    }

                    @Override
                    protected void _onComplete() {
                        view.hideLoading();
                    }
                });
    }
}
