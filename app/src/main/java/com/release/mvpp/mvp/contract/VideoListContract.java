package com.release.mvpp.mvp.contract;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface VideoListContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {

        void requestData(String videoId, int page, boolean isRefresh);
    }
}
