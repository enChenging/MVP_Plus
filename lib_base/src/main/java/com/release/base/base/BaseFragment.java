package com.release.base.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public abstract class BaseFragment extends Fragment implements UiInterfaceFrag, IView {


    /**
     * 视图是否加载完毕
     */
    private boolean isViewPrepare;
    /**
     * 数据是否加载过了
     */
    private boolean hasLoadData;
    private Unbinder mUnbinder;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepare = true;
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        try {
            mUnbinder = ButterKnife.bind(this, view);
        }catch (Exception e){

        }

        initView(view);
        initListener();
        useEventBus();
        lazyLoadDataIfPrepared();
    }

    public boolean useEventBus() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            lazyLoadDataIfPrepared();
    }

    private void lazyLoadDataIfPrepared() {
        if (getUserVisibleHint() && isViewPrepare && !hasLoadData) {
            startNet();
            hasLoadData = true;
        }
    }


    @Override
    public void initView(View view) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void startNet() {

    }

    @Override
    public void onDestroyView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        try {
            if (mUnbinder != null)
                mUnbinder.unbind();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }


    @Override
    public void loadData(Object data,boolean isRefresh) {

    }
}
