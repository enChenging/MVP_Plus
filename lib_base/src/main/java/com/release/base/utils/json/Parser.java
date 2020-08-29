package com.release.base.utils.json;

import android.text.TextUtils;

import retrofit2.HttpException;


public class Parser {
    public static <T extends BaseBean> T parse(String jsonString, Class<T> cls) throws HttpException {
        if (jsonString != null) {
            String json = jsonString;
            if (!TextUtils.isEmpty(json))
                return JsonMananger.jsonToBean(json, cls);

        }
        return null;

    }

    public static <T> T parseStr(String jsonString, Class<T> cls) throws HttpException {
        if (jsonString != null) {
            String json = jsonString;
            if (!TextUtils.isEmpty(json))
                return JsonMananger.jsonToBean(json, cls);
        }
        return null;

    }

}
