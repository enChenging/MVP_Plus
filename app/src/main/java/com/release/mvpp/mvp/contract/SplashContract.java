package com.release.mvpp.mvp.contract;

import android.widget.Button;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.ui.splash.SplashActivity;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface SplashContract {

    interface View extends IView {
        void goGuide();

        void goHome();

        void waitJump();
    }

    interface Presenter extends IPresenter<View> {
        void requestCameraPermissions(SplashActivity activity);

        void jump();

        void countdown(SplashActivity activity, int time, Button mBtnJump);
    }
}
