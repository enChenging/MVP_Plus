package com.release.mvpp.mvp.contract;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.mvp.model.RecommendPageBean;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface RecommendPageContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {

        void requestData(boolean isRefresh, boolean isShowLoading);
    }
}
