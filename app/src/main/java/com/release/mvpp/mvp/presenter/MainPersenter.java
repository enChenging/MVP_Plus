package com.release.mvpp.mvp.presenter;

import android.widget.Toast;

import com.release.base.base.BasePresenter;
import com.release.base.utils.AppManager;
import com.release.mvpp.MainActivity;
import com.release.mvpp.mvp.contract.MainContract;

/**
 * @author Mr.release
 * @create 2019/3/28
 * @Describe
 */
public class MainPersenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {


    private long mExitTime = 0;

    @Override
    public void exit(MainActivity context) {
        if (!mView.closeDrawableLayout()) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {

                AppManager.appExit(context);
            }
        }
    }

}
