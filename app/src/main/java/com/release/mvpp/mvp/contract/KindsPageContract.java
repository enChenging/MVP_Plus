package com.release.mvpp.mvp.contract;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface KindsPageContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {
    }
}
