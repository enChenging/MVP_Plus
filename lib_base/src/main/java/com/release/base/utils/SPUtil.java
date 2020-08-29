package com.release.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr.release
 * @create 2019/3/26
 * @Describe 使用SP的注意事项：
 * 1.不要存放大的key和value！我就不重复三遍了，会引起界面卡、频繁GC、占用内存等等，好自为之！
 * 2.毫不相关的配置项就不要丢在一起了！文件越大读取越慢，不知不觉就被猪队友给坑了；蓝后，放进defalut的那个简直就是愚蠢行为！
 * 3.读取频繁的key和不易变动的key尽量不要放在一起，影响速度。（如果整个文件很小，那么忽略吧，为了这点性能添加维护成本得不偿失）
 * 4.不要乱edit和apply，尽量批量修改一次提交！
 * 5.尽量不要存放JSON和HTML，这种场景请直接使用json！
 */
public class SPUtil {

    private static final String FILE_NAME = "share_date";
    private static SPUtil mInstance;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    public synchronized static SPUtil getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new SPUtil(context.getApplicationContext());
        }

        return mInstance;
    }

    private SPUtil(Context context) {

        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key
     * @param object
     */
    public static void setData(String key, Object object) {

        String type = object == null ? "" : object.getClass().getSimpleName();

        switch (type) {
            case "String":
                mEditor.putString(key, (String) object);
                break;
            case "Integer":
                mEditor.putInt(key, (Integer) object);
                break;
            case "Boolean":
                mEditor.putBoolean(key, (Boolean) object);
                break;
            case "Float":
                mEditor.putFloat(key, (Float) object);
                break;
            case "Long":
                mEditor.putLong(key, (Long) object);
                break;
        }
        mEditor.apply();
    }


    /**
     * 获取SharedPreferences中保存的数据
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getData(String key, Object defaultObject) {
        Object result;
        String type = defaultObject == null ? "" : defaultObject.getClass().getSimpleName();
        switch (type) {
            case "String":
                result = mSharedPreferences.getString(key, (String) defaultObject);
                break;
            case "Integer":
                result = mSharedPreferences.getInt(key, (Integer) defaultObject);
                break;
            case "Boolean":
                result = mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
                break;
            case "Float":
                result = mSharedPreferences.getFloat(key, (Float) defaultObject);
                break;
            case "Long":
                result = mSharedPreferences.getLong(key, (Long) defaultObject);
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public static <T> boolean setListData(String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            mEditor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        mEditor.apply();
        return result;
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public static <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = mSharedPreferences.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean setHashMapData(String key, Map<K, V> map) {
        boolean result;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = mSharedPreferences.getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        Log.e("SharedPreferencesUtil", obj.toString());
        return map;
    }


    public static void clearUserInfo() {
        assert (mEditor != null);
        mEditor.putInt("USER_ID", 0);
        mEditor.apply();
    }
}
