package com.release.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.release.base.base.BaseApplication;


/**
 * @author Mr.release
 * @create 2017/3/22
 * @Describe
 */
public class KeyBoardUtils {

    /**
     * 是否落在 EditText 区域
     */
    public static boolean isHideKeyboard(View view, MotionEvent event) {
        if (view != null && view instanceof EditText) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            //获取现在拥有焦点的控件view的位置，即EditText
            int left = location[0];
            int top = location[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            boolean isInEt = event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom;
            return !isInEt;
        }
        return false;
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity context,View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showKeyboardPopWindow() {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    static boolean isShow;

    private boolean getKeyboardIsShow(Dialog dialog) {

        final View decorView = dialog.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();

                if (displayHight > hight / 3 * 2) {
                    //键盘隐藏
                    isShow = false;
                } else {
                    //键盘显示
                    isShow = true;
                }
            }
        });

        return isShow;
    }


    public static boolean getKeyboardIsShow(Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();

                if (displayHight > hight / 3 * 2) {
                    //键盘隐藏
                    isShow = false;
                } else {
                    //键盘显示
                    isShow = true;
                }
            }
        });

        return isShow;
    }
}
