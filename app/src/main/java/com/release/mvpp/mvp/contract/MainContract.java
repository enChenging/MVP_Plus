package com.release.mvpp.mvp.contract;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.MainActivity;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface MainContract {

    interface View extends IView {
        boolean closeDrawableLayout();
    }

    interface Presenter extends IPresenter<View> {

       void exit(MainActivity context);
    }
}
