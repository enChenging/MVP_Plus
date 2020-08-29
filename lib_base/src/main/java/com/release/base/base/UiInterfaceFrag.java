package com.release.base.base;

import android.view.View;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
interface UiInterfaceFrag {

    int getLayoutId();

    void initView(View view);

    void initListener();

    void startNet();

}
