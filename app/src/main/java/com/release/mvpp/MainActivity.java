package com.release.mvpp;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.release.base.base.BaseMvpActivity;
import com.release.base.utils.ImageLoader;
import com.release.base.utils.StatusBarUtil;
import com.release.base.widget.bottom_navigation.BottomNavigationViewEx;
import com.release.mvpp.mvp.contract.MainContract;
import com.release.mvpp.mvp.presenter.MainPersenter;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import cn.jzvd.Jzvd;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class MainActivity extends BaseMvpActivity<MainContract.View, MainContract.Presenter> implements MainContract.View,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx mBottomNavigation;
    @BindView(R.id.left_navigation)
    NavigationView mLeftNavigation;
    @BindView(R.id.dl_drawer)
    DrawerLayout mDlDrawer;
    private ImageView mHeadImg;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPersenter();
    }

    @Override
    public void initView() {
        super.initView();
        View headerView = mLeftNavigation.getHeaderView(0);
        mLeftNavigation.setItemIconTintList(null);
        mHeadImg = headerView.findViewById(R.id.headImg);

        mBottomNavigation.enableAnimation(false);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mBottomNavigation, navController);

        ImageLoader.loadCircleCrop(this, "https://b-ssl.duitang.com/uploads/item/201802/20/20180220170028_JcYMU.jpeg", mHeadImg, R.mipmap.ic_launcher2);

    }

    @Override
    public void initListener() {

        mDlDrawer.setScrimColor(getResources().getColor(R.color.black_alpha_32));//右边阴影颜色
        mDlDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView != null && drawerView.getTag().equals("left")) {
                    View content = mDlDrawer.getChildAt(0);
                    int offset = (int) (drawerView.getWidth() * slideOffset);
                    content.setTranslationX(offset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mLeftNavigation.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_help_center:
                Toast.makeText(MainActivity.this, "帮助中心", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_camera:
                Toast.makeText(MainActivity.this, "照相机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_gallery:
                Toast.makeText(MainActivity.this, "相册", Toast.LENGTH_SHORT).show();
                break;
        }
        toggle();
        return false;
    }


    public void toggle() {
        int drawerLockMode = mDlDrawer.getDrawerLockMode(GravityCompat.START);
        if (mDlDrawer.isDrawerVisible(GravityCompat.START) && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            mDlDrawer.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDlDrawer.openDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.exit(this);
            return false;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean closeDrawableLayout() {
        if (mDlDrawer.isDrawerVisible(GravityCompat.START)) {
            mDlDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initThemeColor() {
        super.initThemeColor();
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, mDlDrawer, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

}
