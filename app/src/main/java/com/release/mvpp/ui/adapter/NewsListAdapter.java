package com.release.mvpp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.flyco.labelview.LabelView;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.base.utils.ListUtils;
import com.release.base.utils.StringUtils;
import com.release.base.widget.RippleView;
import com.release.mvpp.R;
import com.release.mvpp.mvp.model.NewsInfoBean;
import com.release.mvpp.ui.adapter.item.NewsMultiItem;
import com.release.mvpp.ui.page.news_page.NewsDetailActivity;
import com.release.mvpp.ui.page.news_page.NewsSpecialActivity;
import com.release.mvpp.ui.page.news_page.PhotoAlbumActivity;
import com.release.mvpp.utils.NewsUtils;

import java.util.List;

/**
 * 要闻
 *
 * @author Mr.release
 * @create 2019/3/22
 * @Describe
 */
public class NewsListAdapter extends BaseMultiItemQuickAdapter<NewsMultiItem, BaseViewHolder> {


    private String mTitle;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsListAdapter(List<NewsMultiItem> data, String title) {
        super(data);
        mTitle = title;
        addItemType(NewsMultiItem.ITEM_TYPE_NORMAL, R.layout.adapter_news_list);
        addItemType(NewsMultiItem.ITEM_TYPE_PHOTO_SET, R.layout.adapter_news_photo_set);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsMultiItem item) {
        switch (item.getItemType()) {
            case NewsMultiItem.ITEM_TYPE_NORMAL:
                _handleNewsNormal(helper, item.getNewsBean());
                break;
            case NewsMultiItem.ITEM_TYPE_PHOTO_SET:
                _handleNewsPhotoSet(helper, item.getNewsBean());
                break;
        }
    }

    /**
     * 处理正常的新闻
     *
     * @param holder
     * @param item
     */
    private void _handleNewsNormal(final BaseViewHolder holder, final NewsInfoBean item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterCrop(getContext(), item.getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        // 设置标签
        if (NewsUtils.isNewsSpecial(item.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        } else {
            holder.setVisible(R.id.label_view, false);
        }
        // 波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (NewsUtils.isNewsSpecial(item.getSkipType()))
                    NewsSpecialActivity.start(getContext(), item.getSpecialID(), mTitle);
                else
                    NewsDetailActivity.start(getContext(), item.getPostid(), mTitle);
            }
        });
    }

    /**
     * 处理图片的新闻
     *
     * @param holder
     * @param item
     */
    private void _handleNewsPhotoSet(BaseViewHolder holder, final NewsInfoBean item) {
        ImageView[] newsPhoto = new ImageView[3];
        newsPhoto[0] = holder.getView(R.id.iv_icon_1);
        newsPhoto[1] = holder.getView(R.id.iv_icon_2);
        newsPhoto[2] = holder.getView(R.id.iv_icon_3);
        holder.setVisible(R.id.iv_icon_2, false).setVisible(R.id.iv_icon_3, false);
        ImageLoader.loadCenterCrop(getContext(), item.getImgsrc(), newsPhoto[0], DefIconFactory.provideIcon());
        if (!ListUtils.isEmpty(item.getImgextra())) {
            for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
                newsPhoto[i + 1].setVisibility(View.VISIBLE);
                ImageLoader.loadCenterCrop(getContext(), item.getImgextra().get(i).getImgsrc(),
                        newsPhoto[i + 1], DefIconFactory.provideIcon());
            }
        }
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        // 波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                PhotoAlbumActivity.start(getContext(), item.getPhotosetID());
            }
        });
    }
}
