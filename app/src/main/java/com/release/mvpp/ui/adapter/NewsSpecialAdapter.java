package com.release.mvpp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.flyco.labelview.LabelView;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.base.utils.StringUtils;
import com.release.mvpp.R;
import com.release.mvpp.ui.adapter.item.SpecialItem;
import com.release.mvpp.ui.page.news_page.NewsDetailActivity;
import com.release.mvpp.ui.page.news_page.NewsSpecialActivity;
import com.release.mvpp.utils.NewsUtils;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

/**
 * @author Mr.release
 * @create 2019/4/15
 * @Describe
 */
public class NewsSpecialAdapter extends BaseSectionQuickAdapter<SpecialItem, BaseViewHolder> {

    public NewsSpecialAdapter(int layoutResId, int sectionHeadResId, List<SpecialItem> data) {
        super(sectionHeadResId, data);
        setNormalLayout(layoutResId);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder holder, @NotNull final SpecialItem specialItem) {
        holder.setText(R.id.tv_head, specialItem.getHeader());
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, @NotNull final SpecialItem item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterCrop(getContext(), item.getT().getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getT().getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getT().getSource()))
                .setText(R.id.tv_time, item.getT().getPtime());

        if (NewsUtils.isNewsSpecial(item.getT().getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setBgColor(ContextCompat.getColor(getContext(), R.color.item_special_bg));
            labelView.setText("专题");
        } else if (NewsUtils.isNewsPhotoSet(item.getT().getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setBgColor(ContextCompat.getColor(getContext(), R.color.item_photo_set_bg));
            labelView.setText("图集");
        } else {
            holder.setVisible(R.id.label_view, false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NewsUtils.isNewsSpecial(item.getT().getSkipType())) {
                    NewsSpecialActivity.start(getContext(), item.getT().getSpecialID(), item.getT().getTitle());
                } else {
                    NewsDetailActivity.start(getContext(), item.getT().getPostid(), item.getT().getTitle());
                }
            }
        });
    }


}
