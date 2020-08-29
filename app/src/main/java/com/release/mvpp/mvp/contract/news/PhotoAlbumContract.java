package com.release.mvpp.mvp.contract.news;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface PhotoAlbumContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {

        void requestData(String photoSetId);
    }
}
