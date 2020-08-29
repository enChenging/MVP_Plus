package com.release.mvpp.ui.page.news_page;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.release.base.base.BaseMvpActivity;
import com.release.base.utils.AnimateHelper;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.base.utils.baserx.RxUtil;
import com.release.base.widget.IToolBar;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.news.NewsSpecialContract;
import com.release.mvpp.mvp.model.SpecialInfoBean;
import com.release.mvpp.mvp.presenter.news.NewsSpecialPresenter;
import com.release.mvpp.ui.adapter.NewsSpecialAdapter;
import com.release.mvpp.ui.adapter.item.SpecialItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.release.base.constance.BConstants.NEWS_TYPE_TITLE;
import static com.release.base.constance.BConstants.SPECIAL_KEY;


/**
 * 新闻专题
 *
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsSpecialActivity extends BaseMvpActivity<NewsSpecialContract.View, NewsSpecialContract.Presenter> implements NewsSpecialContract.View {

    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;
    @BindView(R.id.tool_bar)
    IToolBar mToolBar;
    @BindView(R.id.fab_coping)
    FloatingActionButton mFabCoping;

    private NewsSpecialAdapter mAdapter;
    private final int[] mSkipId = new int[20];
    private TagLayout mTagLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Animator mTopBarAnimator;
    private String mSpecialId, mTitle;
    boolean change;
    private List<SpecialItem> mSpecialItems = new ArrayList<>();

    public static void start(Context context, String newsId, String title) {
        Intent intent = new Intent(context, NewsSpecialActivity.class);
        intent.putExtra(SPECIAL_KEY, newsId);
        intent.putExtra(NEWS_TYPE_TITLE, title);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_special;
    }

    @Override
    protected NewsSpecialContract.Presenter createPresenter() {
        return new NewsSpecialPresenter();
    }

    @Override
    public void initView() {
        super.initView();

        mSpecialId = getIntent().getStringExtra(SPECIAL_KEY);
        mTitle = getIntent().getStringExtra(NEWS_TYPE_TITLE);
        mToolBar.setTitleText(mTitle);

        mRvNewsList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvNewsList.setLayoutManager(mLinearLayoutManager);

        mAdapter = new NewsSpecialAdapter(R.layout.adapter_news_list, R.layout.adapter_special_head, null);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        mRvNewsList.setAdapter(mAdapter);
    }

    @Override
    public void startNet() {
        mPresenter.requestData(mSpecialId,true);
    }

    @Override
    public void initListener() {

        int topBarHeight = getResources().getDimensionPixelSize(R.dimen.default_toolbar_height);

        mRvNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0 && change) {//向下滑动
                    if (AnimateHelper.isRunning(mTopBarAnimator))
                        return;
                    mTopBarAnimator = AnimateHelper.doMoveVertical(mToolBar, (int) mToolBar.getTranslationY(), 0, 300);
                    change = false;
                } else if (dy > 0 && !change) {//向上滑动
                    if (AnimateHelper.isRunning(mTopBarAnimator))
                        return;
                    mTopBarAnimator = AnimateHelper.doMoveVertical(mToolBar, 0, -topBarHeight, 300);
                    change = true;
                }
            }
        });
    }

    @Override
    public void loadData(Object data,boolean isRefresh) {
        mSpecialItems.clear();
        mSpecialItems.addAll((List<SpecialItem>) data);
        mAdapter.setList(mSpecialItems);
        _handleTagLayout(mSpecialItems);
    }

    @Override
    public void loadHead(SpecialInfoBean specialBean) {
        View headView = LayoutInflater.from(this).inflate(R.layout.head_special, null);
        ImageView mIvBanner = headView.findViewById(R.id.iv_banner);
        IToolBar toolBar = headView.findViewById(R.id.toolbar);
        toolBar.setTitleText(mTitle);

        ImageLoader.loadFitCenter(this, specialBean.getBanner(), mIvBanner, DefIconFactory.provideIcon());

        // 添加导语
        if (!TextUtils.isEmpty(specialBean.getDigest())) {
            ViewStub stub = headView.findViewById(R.id.vs_digest);
            assert stub != null;
            stub.inflate();
            TextView tvDigest = headView.findViewById(R.id.tv_digest);
            tvDigest.setText(specialBean.getDigest());
        }
        mTagLayout = headView.findViewById(R.id.tag_layout);
        mAdapter.addHeaderView(headView);
    }


    /**
     * 处理导航标签
     *
     * @param specialItems
     */
    private void _handleTagLayout(List<SpecialItem> specialItems) {
        Observable.fromIterable(specialItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<SpecialItem>() {
                    int i = 0;
                    int index = mAdapter.getHeaderLayoutCount();  // 存在一个 HeadView 所以从1算起

                    @Override
                    public boolean test(SpecialItem specialItem) throws Exception {

                        if (specialItem.isHeader()) {
                            // 记录头部的列表索引值，用来跳转
                            mSkipId[i++] = index;
                        }
                        index++;
                        return specialItem.isHeader();
                    }
                })
                .map(new Function<SpecialItem, String>() {
                    @Override
                    public String apply(SpecialItem specialItem) throws Exception {
                        return _clipHeadStr(specialItem.getHeader());
                    }
                })
                .as(RxUtil.bindLifecycle(this))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mTagLayout.addTag(s);
                    }
                });

        mTagLayout.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text, @TagView.TagMode int tagMode) {
                // 跳转到对应position,比scrollToPosition（）精确
                mLinearLayoutManager.scrollToPositionWithOffset(mSkipId[position], 0);
            }
        });
    }

    private String _clipHeadStr(String headStr) {
        String head = null;
        int index = headStr.indexOf(" ");
        if (index != -1) {
            head = headStr.substring(index, headStr.length());
        }
        return head;
    }

    @OnClick(R.id.fab_coping)
    public void onViewClicked() {
        mLinearLayoutManager.scrollToPosition(0);
    }


}
