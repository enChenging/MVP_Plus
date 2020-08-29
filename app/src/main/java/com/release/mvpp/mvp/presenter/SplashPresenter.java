package com.release.mvpp.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.base.constance.BConstants;
import com.release.base.utils.InstallUtil;
import com.release.base.utils.RxHelper;
import com.release.base.utils.SPUtil;
import com.release.base.utils.baserx.CommonSubscriber;
import com.release.base.utils.baserx.RxUtil;
import com.release.base.widget.dialog.NoticeDialog;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.SplashContract;
import com.release.mvpp.ui.splash.SplashActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author Mr.releasel
 * @create 2019/3/27
 * @Describe
 */
public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {


    @SuppressLint("CheckResult")
    @Override
    public void countdown(SplashActivity activity, int time, Button mBtnJump) {
        RxHelper.countdown(time)
                .doOnSubscribe((disposable) -> mBtnJump.setText("跳过(6)"))
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .as(RxUtil.bindLifecycle((LifecycleOwner) mView))
                .subscribeWith(new CommonSubscriber<Long>() {
                    @Override
                    protected void _onNext(Long aLong) {
                        mBtnJump.setText("跳过(" + aLong + ")");
                    }

                    @Override
                    protected void _onError(String message) {
                        Logger.e("_onError: " + message);
                    }

                    @Override
                    protected void _onComplete() {
                        Logger.i("_onComplete: ");
                        if (activity.isVisiable)
                            mView.goHome();
                    }
                });
    }




    @Override
    public void jump() {
        Boolean loginFirst = (Boolean) SPUtil.getData(BConstants.LOGIN_FIRST, true);
        if (loginFirst) {
            mView.goGuide();
            SPUtil.setData(BConstants.LOGIN_FIRST, false);
        } else {
            mView.waitJump();
        }
    }

    //-----------------------------------------------RxPermissions--------------------------------------------
    private boolean isNever;
    public  static  boolean hasPermission;
    private SplashActivity mActivity;

    @SuppressLint("CheckResult")
    @Override
    public void requestCameraPermissions(SplashActivity activity) {
        mActivity = activity;
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEachCombined
                (Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        hasPermission = true;
                        jump();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        showNotice(activity, activity.getResources().getString(R.string.rationale_wr), isNever);
                    } else {
                        isNever = true;
                        showNotice(activity, activity.getResources().getString(R.string.rationale_ask_again), isNever);
                    }
                });

    }

    public void showNotice(SplashActivity context, String content, boolean isNever) {
        NoticeDialog.show(context, content, (v -> noticeListener(context, v, isNever)));
    }

    public void noticeListener(SplashActivity context, View v, boolean isNever) {
        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.tv_cancel:
                context.mBtnPermission.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ok:
                if (isNever)
                    InstallUtil.startAppSettings(context);
                else
                    requestCameraPermissions(mActivity);
                break;
        }
        NoticeDialog.dismissDialog();
    }
}
