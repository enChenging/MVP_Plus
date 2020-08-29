package com.release.base.base;

/**
 * @author Mr.release
 * @create 2019/4/19
 * @Describe
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    protected V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
