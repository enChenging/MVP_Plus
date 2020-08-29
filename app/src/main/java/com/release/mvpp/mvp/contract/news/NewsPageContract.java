package com.release.mvpp.mvp.contract.news;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.dao.NewsTypeInfo;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface NewsPageContract {

    interface View extends IView {

        void loadData(List<NewsTypeInfo> newsTypeInfos);
    }

    interface Presenter extends IPresenter<View> {

        void requestData();
    }
}
