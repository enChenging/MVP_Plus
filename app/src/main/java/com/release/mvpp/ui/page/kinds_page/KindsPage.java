package com.release.mvpp.ui.page.kinds_page;

import com.release.base.base.BaseMvpFragment;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.KindsPageContract;
import com.release.mvpp.mvp.presenter.KindsPagePresenter;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class KindsPage extends BaseMvpFragment<KindsPageContract.View, KindsPageContract.Presenter> implements KindsPageContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.page_kinds;
    }

    @Override
    protected KindsPageContract.Presenter createPresenter() {
        return new KindsPagePresenter();
    }
}
