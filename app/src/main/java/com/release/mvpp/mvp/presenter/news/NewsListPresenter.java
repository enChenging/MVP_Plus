package com.release.mvpp.mvp.presenter.news;

import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.http.RetrofitHelper;
import com.release.mvpp.mvp.contract.news.NewsListContract;
import com.release.mvpp.mvp.model.NewsInfoBean;
import com.release.mvpp.ui.adapter.item.NewsMultiItem;
import com.release.mvpp.utils.NewsUtils;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author Mr.release
 * @create 2019/3/29
 * @Describe
 */
public class NewsListPresenter extends BasePresenter<NewsListContract.View> implements NewsListContract.Presenter {


    @Override
    public void requestData(String newsId, int page, boolean isRefresh) {
        Logger.e("NewsListFragment", "loadData---mNewsId: " + newsId);

        Flowable<List<NewsMultiItem>> flowable = RetrofitHelper
                .getImportantNewAPI(newsId, page)
                .filter(new Predicate<NewsInfoBean>() {
                    @Override
                    public boolean test(NewsInfoBean newsInfo) throws Exception {
                        if (NewsUtils.isAbNews(newsInfo))
                            mView.loadAdData(newsInfo);
                        return !NewsUtils.isAbNews(newsInfo);//决定了是否将结果返回给订阅者
                    }
                })
                .compose(observableTransformer);

        HttpUtils.ext(flowable, mView, page == 0 && !isRefresh);

    }

    private FlowableTransformer<NewsInfoBean, List<NewsMultiItem>> observableTransformer = new FlowableTransformer<NewsInfoBean, List<NewsMultiItem>>() {

        @Override
        public Publisher<List<NewsMultiItem>> apply(Flowable<NewsInfoBean> upstream) {

            return upstream.map(new Function<NewsInfoBean, NewsMultiItem>() {
                @Override
                public NewsMultiItem apply(NewsInfoBean newsInfo) throws Exception {

                    if (NewsUtils.isNewsPhotoSet(newsInfo.getSkipType())) {
                        return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET, newsInfo);
                    }
                    return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL, newsInfo);
                }
            })
                    .toList()
                    .toFlowable();
        }
    };
}
