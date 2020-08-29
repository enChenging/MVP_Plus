package com.release.mvpp.mvp.presenter.news;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.release.base.base.BasePresenter;
import com.release.mvpp.App;
import com.release.mvpp.dao.NewsTypeInfo;
import com.release.mvpp.dao.NewsTypeInfoDao;
import com.release.mvpp.mvp.contract.news.NewsPageContract;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class NewsPagePresenter extends BasePresenter<NewsPageContract.View> implements NewsPageContract.Presenter {

    @Override
    public void requestData() {
        NewsTypeInfoDao newsTypeInfoDao = App.getInstance().mDaoSession.getNewsTypeInfoDao();
        List<NewsTypeInfo> newsTypeInfos = newsTypeInfoDao.loadAll();

        String s = JSON.toJSONString(newsTypeInfos);
        Logger.i("loadData: " + newsTypeInfos.size());
        Logger.i("s: " + s);
        mView.loadData(newsTypeInfos);
    }
}
