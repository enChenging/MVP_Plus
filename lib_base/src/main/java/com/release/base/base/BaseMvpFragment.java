package com.release.base.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.release.base.R;
import com.release.base.utils.ToastUtils;
import com.release.base.widget.EmptyLayout;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public abstract class BaseMvpFragment<V extends IView, P extends IPresenter> extends BaseFragment implements IView {

    protected P mPresenter;
    private EmptyLayout mEmptyLayout;

    protected abstract P createPresenter();

    @Override
    public void initView(View view) {
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        super.initView(view);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyLayout = view.findViewById(R.id.empty_layout);
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
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter = null;
    }


}
