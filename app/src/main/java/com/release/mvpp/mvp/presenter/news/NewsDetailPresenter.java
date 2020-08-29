package com.release.mvpp.mvp.presenter.news;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.base.utils.ListUtils;
import com.release.mvpp.mvp.model.NewsDetailInfoBean;
import com.release.mvpp.http.HttpUtils;
import com.release.mvpp.http.RetrofitHelper;
import com.release.mvpp.mvp.contract.news.NewsDetailContract;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter {

    private static final String HTML_IMG_TEMPLATE = "<img src=\"http\" />";

    @SuppressLint("CheckResult")
    @Override
    public void requestData(String newsId, boolean isShowLoading) {
        Logger.i("newsId: " + newsId);
        Flowable<NewsDetailInfoBean> flowable = RetrofitHelper
                .getNewsDetailAPI(newsId)
                .doOnNext(new Consumer<NewsDetailInfoBean>() {
                    @Override
                    public void accept(NewsDetailInfoBean newsDetailInfoBean) throws Exception {
                        String s = JSON.toJSONString(newsDetailInfoBean);
                        Logger.i("accept: " + s);
                        _handleRichTextWithImg(newsDetailInfoBean);
                    }
                });
        HttpUtils.ext(flowable, mView, true,isShowLoading);
    }

    /**
     * 处理富文本包含图片的情况
     *
     * @param newsDetailBean 原始数据
     */
    private void _handleRichTextWithImg(NewsDetailInfoBean newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getImg())) {
            String body = newsDetailBean.getBody();
            for (NewsDetailInfoBean.ImgBean imgEntity : newsDetailBean.getImg()) {
                String ref = imgEntity.getRef();
                String src = imgEntity.getSrc();
//                LogUtils.i(TAG, "imgEntity: " + imgEntity.toString());
                String img = HTML_IMG_TEMPLATE.replace("http", src);
                body = body.replaceAll(ref, img);
//                LogUtils.i(TAG, "img: " + img);
//                LogUtils.i(TAG, "body: " + body);
            }
            newsDetailBean.setBody(body);
        }
    }
}
