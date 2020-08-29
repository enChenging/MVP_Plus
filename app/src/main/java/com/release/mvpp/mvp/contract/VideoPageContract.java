package com.release.mvpp.mvp.contract;

import androidx.fragment.app.Fragment;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;

import java.util.ArrayList;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface VideoPageContract {

    interface View extends IView {

        void loadData(ArrayList<Fragment> fragments, String[] strings);
    }

    interface Presenter extends IPresenter<View> {

        void requestData();
    }
}
