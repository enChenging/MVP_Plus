package com.release.mvpp.ui.guide;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.release.base.base.BaseActivity;
import com.release.base.utils.DensityUtil;
import com.release.base.utils.StatusBarUtil;
import com.release.base.widget.pageTransformer.CubeOutTransformer;
import com.release.mvpp.MainActivity;
import com.release.mvpp.R;
import com.release.mvpp.ui.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class GuideActivity extends BaseActivity {

    @BindView(R.id.view_viewpager)
    ViewPager mViewViewpager;
    @BindView(R.id.bt_home)
    Button mBtHome;
    @BindView(R.id.dot_group)
    LinearLayout mDotGroup;
    @BindView(R.id.dot_focus)
    View mDotFocus;

    private int dot_width;
    public List<ImageView> imageList = new ArrayList<>();
    int[] images = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GuideActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        super.initView();
        imageViews(this, mDotGroup);
        mViewViewpager.setAdapter(new GuideViewPagerAdapter(imageList));
        //效果
//        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
//        viewpager.setPageTransformer(true, new DepthPageTransformer());
//        viewpager.setPageTransformer(true, new RotatePageTransformer());
        mViewViewpager.setPageTransformer(true, new CubeOutTransformer());
    }


    @Override
    public void initListener() {
        mBtHome.setOnClickListener((v -> goHome()));

        mViewViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int translate = (int) (dot_width * (position + positionOffset));
                mDotFocus.setTranslationX(translate);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageList.size() - 1) {
                    mBtHome.setVisibility(View.VISIBLE);
                } else {
                    mBtHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDotFocus.postDelayed(() -> dot_width = mDotGroup.getChildAt(1).getLeft() - mDotGroup.getChildAt(0).getLeft(), 5);
    }


    public void goHome() {
        MainActivity.start(this);
    }

    @Override
    protected void initThemeColor() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
    }

    public void imageViews(Context context, LinearLayout mDotGroup) {
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(context);
            iv.setBackgroundResource(images[i]);
            imageList.add(iv);

            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(context, 6), DensityUtil.dip2px(context, 6));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(context, 10);
            }
            view.setBackgroundResource(R.drawable.ic_dots_blue);
            view.setLayoutParams(params);
            mDotGroup.addView(view);
        }
    }
}
