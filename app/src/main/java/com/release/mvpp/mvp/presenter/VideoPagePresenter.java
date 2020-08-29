package com.release.mvpp.mvp.presenter;

import com.release.base.base.BasePresenter;
import com.release.mvpp.mvp.contract.VideoPageContract;
import com.release.mvpp.ui.page.video_page.VideoListFragment;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;

/**
 * @author Mr.release
 * @create 2019/4/22
 * @Describe
 */
public class VideoPagePresenter extends BasePresenter<VideoPageContract.View> implements VideoPageContract.Presenter {

    private final String[] VIDEO_ID = new String[]{
            "V9LG4B3A0", "V9LG4E6VR", "V9LG4CHOR", "00850FRB"
    };
    private final String[] VIDEO_TITLE = new String[]{
            "热点", "搞笑", "娱乐", "精品"
    };

    @Override
    public void requestData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < VIDEO_ID.length; i++) {
            fragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
        }

        mView.loadData(fragments, VIDEO_TITLE);
    }
}
