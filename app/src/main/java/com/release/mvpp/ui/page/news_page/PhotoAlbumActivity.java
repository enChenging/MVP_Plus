package com.release.mvpp.ui.page.news_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.dl7.drag.DragSlopLayout;
import com.release.base.base.BaseMvpActivity;
import com.release.base.utils.StatusBarUtil;
import com.release.base.widget.IToolBar;
import com.release.base.widget.PhotoViewPager;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.news.PhotoAlbumContract;
import com.release.mvpp.mvp.model.PhotoSetInfoBean;
import com.release.mvpp.mvp.presenter.news.PhotoAlbumPresenter;
import com.release.mvpp.ui.adapter.PhotoSetAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.release.base.constance.BConstants.PHOTO_SET_KEY;

/**
 * 图集
 *
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class PhotoAlbumActivity extends BaseMvpActivity<PhotoAlbumContract.View, PhotoAlbumContract.Presenter> implements PhotoAlbumContract.View {


    PhotoSetAdapter mAdapter;
    @BindView(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.ll_bottom_content)
    LinearLayout mLlBottomContent;
    @BindView(R.id.tool_bar)
    IToolBar mToolBar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;

    private boolean mIsHideToolbar = false;
    private List<PhotoSetInfoBean.PhotosBean> mPhotos;
    private String mPhotoSetId;

    public static void start(Context context, String newsId) {
        Intent intent = new Intent(context, PhotoAlbumActivity.class);
        intent.putExtra(PHOTO_SET_KEY, newsId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_album;
    }

    @Override
    protected PhotoAlbumContract.Presenter createPresenter() {
        return new PhotoAlbumPresenter();
    }

    @Override
    public void initView() {
        super.initView();
        mPhotoSetId = getIntent().getStringExtra(PHOTO_SET_KEY);

        mToolBar.setToolBarBackgroundColor(R.color.transparent)
                .setBackDrawable(R.drawable.toolbar_back_white)
                .setTitleColor(R.color.White);

        mAdapter = new PhotoSetAdapter(this);
    }

    @Override
    public void startNet() {

        mPresenter.requestData(mPhotoSetId,true);
    }

    @Override
    public void loadData(Object data, boolean isRefresh) {
        PhotoSetInfoBean photoSetBean = (PhotoSetInfoBean) data;

        String title = photoSetBean.getSetname();
        mToolBar.setTitleText(title);

        if (title.length() > 10) {
            mToolBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mToolBar.setTitleSelected(true);
                }
            }, 2000);
        }

        List<String> imgUrls = new ArrayList<>();
        mPhotos = photoSetBean.getPhotos();
        for (PhotoSetInfoBean.PhotosBean photo : mPhotos) {
            imgUrls.add(photo.getImgurl());
        }

        mAdapter.setData(imgUrls);
        mVpPhoto.setAdapter(mAdapter);
        mVpPhoto.setOffscreenPageLimit(imgUrls.size());

        mTvCount.setText(mPhotos.size() + "");
        mTvIndex.setText(1 + "/");
        mTvContent.setText(mPhotos.get(0).getNote());


        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvContent.setText(mPhotos.get(position).getNote());
                mTvIndex.setText((position + 1) + "/");
            }
        });
        mAdapter.setTapListener(new PhotoSetAdapter.OnTapListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    mDragLayout.scrollOutScreen(300);
                    mToolBar.animate().translationY(-mToolBar.getBottom()).setDuration(300);
                } else {
                    mDragLayout.scrollInScreen(300);
                    mToolBar.animate().translationY(0).setDuration(300);
                }
            }
        });
    }

    @Override
    protected void initThemeColor() {
        super.initThemeColor();
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.Black));
    }

}
