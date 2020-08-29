package com.release.mvpp.mvp.contract.news;

import com.release.base.base.IPresenter;
import com.release.base.base.IView;
import com.release.mvpp.mvp.model.SpecialInfoBean;
import com.release.mvpp.ui.adapter.item.SpecialItem;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public interface NewsSpecialContract {


    interface View extends IView {

        void loadHead(SpecialInfoBean specialBean);
    }

    interface Presenter extends IPresenter<View> {

        void requestData(String specialId);
    }
}
