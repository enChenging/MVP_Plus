package com.release.mvpp.mvp.contract.news;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.mvp.model.NewsInfoBean;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface NewsListContract {


    interface View extends IView {

        void loadAdData(NewsInfoBean newsInfoBean);
    }

    interface Presenter extends IPresenter<View> {

        void requestData(String newsId, int page, boolean isRefresh);
    }
}
