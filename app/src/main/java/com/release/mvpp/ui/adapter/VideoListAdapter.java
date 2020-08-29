package com.release.mvpp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.release.base.utils.DefIconFactory;
import com.release.base.utils.ImageLoader;
import com.release.mvpp.R;
import com.release.mvpp.dao.VideoInfo;
import java.util.List;

import androidx.annotation.Nullable;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @author Mr.release
 * @create 2019/4/16
 * @Describe
 */
public class VideoListAdapter extends BaseQuickAdapter<VideoInfo, BaseViewHolder> {


    public VideoListAdapter(int layoutResId, @Nullable List<VideoInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoInfo item) {

        JzvdStd videoplayer = holder.getView(R.id.videoplayer);

        holder.setText(R.id.tv_title, item.getTitle());

        videoplayer.setUp(item.getMp4_url(), item.getTitle(), Jzvd.SCREEN_NORMAL);

        ImageLoader.loadFitCenter(getContext(), item.getCover(), videoplayer.thumbImageView, DefIconFactory.provideIcon());
    }
}
