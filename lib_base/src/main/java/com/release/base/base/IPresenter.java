package com.release.base.base;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface IPresenter<V extends IView> {

    void attachView(V view);

    void detachView();
}
