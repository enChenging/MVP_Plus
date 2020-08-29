package com.release.mvpp.mvp.presenter.news;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.http.RetrofitHelper;
import com.release.mvpp.mvp.contract.news.NewsSpecialContract;
import com.release.mvpp.mvp.model.NewsItemInfoBean;
import com.release.mvpp.mvp.model.SpecialInfoBean;
import com.release.mvpp.ui.adapter.item.SpecialItem;

import org.reactivestreams.Publisher;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsSpecialPresenter extends BasePresenter<NewsSpecialContract.View> implements NewsSpecialContract.Presenter {

    @Override
    public void requestData(String specialId, boolean isShowLoading) {
        Flowable<List<SpecialItem>> flowable = RetrofitHelper
                .getNewsSpecialAPI(specialId)
                .flatMap(new Function<SpecialInfoBean, Publisher<SpecialItem>>() {
                    @Override
                    public Publisher<SpecialItem> apply(SpecialInfoBean specialInfoBean) throws Exception {
                        String s = JSON.toJSONString(specialInfoBean);
                        Logger.i("SpecialInfoBean: " + s);
                        mView.loadHead(specialInfoBean);
                        return _convertSpecialBeanToItem(specialInfoBean);
                    }
                })
                .toList()
                .toFlowable();

        HttpUtils.ext(flowable, mView, true,isShowLoading);
    }


    private Flowable<SpecialItem> _convertSpecialBeanToItem(SpecialInfoBean specialBean) {
        // 这边 +1 是接口数据还有个 topicsplus 的字段可能是穿插在 topics 字段列表中间。这里没有处理 topicsplus
        final SpecialItem[] specialItems = new SpecialItem[specialBean.getTopics().size() + 1];


        return Flowable
                .fromIterable(specialBean.getTopics())
                // 获取头部
                .doOnNext(new Consumer<SpecialInfoBean.TopicsBean>() {
                    @Override
                    public void accept(SpecialInfoBean.TopicsBean topicsEntity) throws Exception {
                        specialItems[topicsEntity.getIndex() - 1] = new SpecialItem(true,
                                topicsEntity.getIndex() + "/" + specialItems.length + " " + topicsEntity.getTname());
                    }
                })
                // 排序
                .toSortedList(new Comparator<SpecialInfoBean.TopicsBean>() {
                    @Override
                    public int compare(SpecialInfoBean.TopicsBean o1, SpecialInfoBean.TopicsBean o2) {
                        return o1.getIndex() - o2.getIndex();
                    }
                })
                .toFlowable()
                // 拆分
                .flatMap(new Function<List<SpecialInfoBean.TopicsBean>, Publisher<SpecialInfoBean.TopicsBean>>() {
                    @Override
                    public Publisher<SpecialInfoBean.TopicsBean> apply(List<SpecialInfoBean.TopicsBean> topicsEntities) throws Exception {
                        return Flowable.fromIterable(topicsEntities);
                    }
                })
                .flatMap(new Function<SpecialInfoBean.TopicsBean, Publisher<SpecialItem>>() {
                    @Override
                    public Publisher<SpecialItem> apply(SpecialInfoBean.TopicsBean topicsEntity) throws Exception {
                        // 转换并在每个列表项增加头部
                        return Flowable.fromIterable(topicsEntity.getDocs())
                                .map(new Function<NewsItemInfoBean, SpecialItem>() {
                                    @Override
                                    public SpecialItem apply(NewsItemInfoBean newsItemInfoBean) throws Exception {
                                        return new SpecialItem(newsItemInfoBean);
                                    }
                                })
                                .startWith(specialItems[topicsEntity.getIndex() - 1]);
                    }
                });
    }


}
