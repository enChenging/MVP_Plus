package com.release.mvpp.ui.page.news_page;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.release.base.base.BaseMvpActivity;
import com.release.base.utils.AnimateHelper;
import com.release.base.utils.ListUtils;
import com.release.base.widget.EmptyLayout;
import com.release.base.widget.IToolBar;
import com.release.base.widget.PullScrollView;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.news.NewsDetailContract;
import com.release.mvpp.mvp.model.NewsDetailInfoBean;
import com.release.mvpp.mvp.presenter.news.NewsDetailPresenter;
import com.release.mvpp.utils.NewsUtils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.release.base.constance.BConstants.NEWS_ID_KEY;
import static com.release.base.constance.BConstants.NEWS_TYPE_TITLE;

/**
 * 新闻详情
 *
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsDetailActivity extends BaseMvpActivity<NewsDetailContract.View, NewsDetailContract.Presenter> implements NewsDetailContract.View {


    @BindView(R.id.tv_title_content)
    TextView mTvTitleContent;
    @BindView(R.id.tv_source)
    TextView mTvSource;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.vs_related_content)
    ViewStub mVsRelatedContent;
    @BindView(R.id.tv_next_title)
    TextView mTvNextTitle;
    @BindView(R.id.ll_foot_view)
    LinearLayout mLlFootView;
    @BindView(R.id.scroll_view)
    PullScrollView mScrollView;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;
    @BindView(R.id.tool_bar)
    IToolBar mToolBar;
    @BindView(R.id.fab_coping)
    FloatingActionButton mFabCoping;
    private int mMinScrollSlop;
    private Animator mTopBarAnimator;
    private int mLastScrollY = 0;
    private String mNextNewsId;
    private String mNewsId, mTitle;


    public static void start(Context context, String newsId, String title) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        intent.putExtra(NEWS_TYPE_TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    private void startInside(String newsId) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_detail;
    }

    @Override
    protected NewsDetailContract.Presenter createPresenter() {
        return new NewsDetailPresenter();
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        mNewsId = intent.getStringExtra(NEWS_ID_KEY);
        mTitle = intent.getStringExtra(NEWS_TYPE_TITLE);

        RichText.initCacheDir(this);
        RichText.debugMode = false;

        // 最小触摸滑动距离
        mMinScrollSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    @Override
    public void startNet() {
        mPresenter.requestData(mNewsId);
    }

    @Override
    public void initListener() {
        int topBarHeight = getResources().getDimensionPixelSize(R.dimen.default_toolbar_height);


        mToolBar.setTitleText(mTitle);
        if (mTitle.length() > 10) {
            mToolBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mToolBar.setTitleSelected(true);
                }
            }, 2000);
        }

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > topBarHeight) {
                    if (AnimateHelper.isRunning(mTopBarAnimator)) {
                        return;
                    }
                    if (Math.abs(scrollY - mLastScrollY) > mMinScrollSlop) {
                        boolean isPullUp = scrollY > mLastScrollY;
                        mLastScrollY = scrollY;
                        if (isPullUp && mToolBar.getTranslationY() != -topBarHeight) {//往上滑
                            mTopBarAnimator = AnimateHelper.doMoveVertical(mToolBar, (int) mToolBar.getTranslationY(),
                                    -topBarHeight, 300);
                        } else if (!isPullUp && mToolBar.getTranslationY() != 0) {//往下滑
                            mTopBarAnimator = AnimateHelper.doMoveVertical(mToolBar, (int) mToolBar.getTranslationY(),
                                    0, 300);
                        }
                    }
                }
            }
        });
        mScrollView.setFootView(mLlFootView);
        mScrollView.setPullListener(new PullScrollView.OnPullListener() {

            @Override
            public boolean isDoPull() {
                if (mEmptyLayout.getEmptyStatus() != EmptyLayout.STATUS_HIDE) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean handlePull() {
                if (TextUtils.isEmpty(mNextNewsId)) {
                    return false;
                } else {
                    startInside(mNextNewsId);
                    return true;
                }
            }
        });
    }


    @Override
    public void loadData(Object data) {
        NewsDetailInfoBean newsDetailInfoBean = (NewsDetailInfoBean) data;

        mTvTitleContent.setText(newsDetailInfoBean.getTitle());
        mTvSource.setText(newsDetailInfoBean.getSource());
        mTvTime.setText(newsDetailInfoBean.getPtime());

        RichText.from(newsDetailInfoBean.getBody()).into(mTvContent);
        _handleSpInfo(newsDetailInfoBean.getSpinfo());
        _handleRelatedNews(newsDetailInfoBean);
    }

    /**
     * 处理关联的内容
     *
     * @param spinfo
     */
    private void _handleSpInfo(List<NewsDetailInfoBean.SpinfoBean> spinfo) {
        if (!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = findViewById(R.id.vs_related_content);
            assert stub != null;
            stub.inflate();
            TextView tvType = findViewById(R.id.tv_type);
            TextView tvRelatedContent = findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    // 这里处理超链接的点击
                    .urlClick(new OnUrlClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            String newsId = NewsUtils.clipNewsIdFromUrl(url);
                            if (newsId != null) {
                                mPresenter.requestData(newsId);
                            }
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    /**
     * 处理关联新闻
     *
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailInfoBean newsDetailBean) {
        if (ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            mTvNextTitle.setText("没有相关文章了");
        } else {
            mNextNewsId = newsDetailBean.getRelative_sys().get(0).getId();
            mTvNextTitle.setText(newsDetailBean.getRelative_sys().get(0).getTitle());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }

    @OnClick(R.id.fab_coping)
    public void onViewClicked() {
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

}
