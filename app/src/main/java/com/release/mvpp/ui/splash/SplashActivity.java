package com.release.mvpp.ui.splash;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.release.base.base.BaseMvpActivity;
import com.release.base.utils.AppManager;
import com.release.base.utils.StatusBarUtil;
import com.release.mvpp.MainActivity;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.SplashContract;
import com.release.mvpp.mvp.presenter.SplashPresenter;
import com.release.mvpp.ui.guide.GuideActivity;

import butterknife.BindView;
import butterknife.OnClick;
import yanzhikai.textpath.PathAnimatorListener;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.ArrowPainter;
import yanzhikai.textpath.painter.PenPainter;


/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class SplashActivity extends BaseMvpActivity<SplashContract.View, SplashContract.Presenter> implements SplashContract.View {

    @BindView(R.id.btn_jump)
    Button mBtnJump;
    @BindView(R.id.btn_permission)
    public Button mBtnPermission;
    @BindView(R.id.stpv_wa)
    SyncTextPathView mStpvWa;
    @BindView(R.id.stpv_cbh)
    SyncTextPathView mStpvCbh;
    public boolean isVisiable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected SplashContract.Presenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void initView() {
        super.initView();
        if (Build.VERSION.SDK_INT >= 23)
            mPresenter.requestCameraPermissions(this);
        else
            mPresenter.jump();
        initStpv();
    }

    private void initStpv() {
        mStpvCbh.setPathPainter(new ArrowPainter());
        mStpvCbh.startAnimation(0, 1);

        mStpvWa.setPathPainter(new PenPainter());
        mStpvWa.startAnimation(0, 1);
        //设置动画播放完后填充颜色
        mStpvWa.setAnimatorListener(new PathAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    mStpvWa.showFillColorText();//填充文字颜色
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void goGuide() {
        GuideActivity.start(this);
    }

    @Override
    public void goHome() {
        MainActivity.start(this);
    }

    @OnClick({R.id.btn_jump, R.id.btn_permission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_jump:
                if (Build.VERSION.SDK_INT >= 23 && !SplashPresenter.hasPermission)
                    mPresenter.requestCameraPermissions(this);
                else
                    goHome();
                break;
            case R.id.btn_permission:
                if (Build.VERSION.SDK_INT >= 23)
                    mPresenter.requestCameraPermissions(this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisiable = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.appExit(this);
    }

    @Override
    public void waitJump() {
        mBtnJump.setVisibility(View.VISIBLE);
        mBtnPermission.setVisibility(View.GONE);
    }

    @Override
    protected void initThemeColor() {
        StatusBarUtil.setTranslucent(this, 0);
    }

}
