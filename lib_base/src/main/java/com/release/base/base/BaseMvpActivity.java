package com.release.base.base;

import android.os.Bundle;

import com.release.base.R;
import com.release.base.utils.ToastUtils;
import com.release.base.widget.EmptyLayout;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public abstract class BaseMvpActivity<V extends IView, P extends IPresenter<V>>  extends BaseActivity implements IView{

    protected P mPresenter;
    protected EmptyLayout mEmptyLayout;
    protected abstract P createPresenter();

    @Override
    public void initView() {
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        super.initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmptyLayout = findViewById(R.id.empty_layout);
    }

    @Override
    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void showError() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(new EmptyLayout.OnRetryListener() {
                @Override
                public void onRetry() {
                    startNet();
                }
            });
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter = null;
    }

    @Override
    public void loadData(Object data) {

    }
}
