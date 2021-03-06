package com.release.mvpp.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.mvpp.R;
import com.release.mvpp.mvp.model.RecommendPageBean;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class RecommendAdapter extends BaseQuickAdapter<RecommendPageBean.NewslistBean, BaseViewHolder> {


    public RecommendAdapter(int layoutResId, @Nullable List<RecommendPageBean.NewslistBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendPageBean.NewslistBean item) {

        ImageLoader.loadFitCenter(getContext(), item.getPicUrl(), helper.getView(R.id.iv_tuijian), DefIconFactory.provideIcon());
        helper.setText(R.id.tv_tuijian_title, item.getTitle());
        helper.setText(R.id.tv_tuijian_time, item.getCtime());
    }
}
