package com.release.mvpp.ui.adapter.item;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.release.mvpp.mvp.model.NewsItemInfoBean;

/**
 * java请自定义类，继承于JSectionEntity抽象类。封装一遍自己的数据类
 * <p>
 * kotlin，数据类请直接实现SectionEntity接口即可，无需封装。
 */
public class SpecialItem extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private NewsItemInfoBean t;

    public SpecialItem(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public SpecialItem(NewsItemInfoBean newsItemBean) {
        this.isHeader = false;
        this.header = "";
        this.t = newsItemBean;
    }

    public String getHeader() {
        return header;
    }

    public NewsItemInfoBean getT() {
        return t;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
