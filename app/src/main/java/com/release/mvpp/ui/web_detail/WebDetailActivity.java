package com.release.mvpp.ui.web_detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.release.base.base.BaseMvpActivity;
import com.release.base.widget.IToolBar;
import com.release.mvpp.R;
import com.release.mvpp.mvp.contract.WebDetailContract;
import com.release.mvpp.mvp.presenter.WebDetailActivityPresenter;

import butterknife.BindView;

import static com.release.base.constance.BConstants.RECOMMEND_CTIME;
import static com.release.base.constance.BConstants.RECOMMEND_DESC;
import static com.release.base.constance.BConstants.RECOMMEND_HTML;
import static com.release.base.constance.BConstants.RECOMMEND_TITLE;


/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */

public class WebDetailActivity extends BaseMvpActivity<WebDetailContract.View, WebDetailContract.Presenter> implements WebDetailContract.View {

    @BindView(R.id.tool_bar)
    IToolBar mToolBar;
    @BindView(R.id.tv_title_content)
    TextView mTvTitleContent;
    @BindView(R.id.tv_source)
    TextView mTvSource;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.webView)
    WebView mWebView;


    private WebSettings mWebSettings;
    boolean isConnected = true;//TODO：模拟网络连接
    private String mRecomTime;
    private String mRecomDesc;
    private String mRecomHtml;
    private String mRecomTitle;

    public static void start(Context context, String title, String cTime, String desc, String html) {

        Intent intent = new Intent(context, WebDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(RECOMMEND_TITLE, title);
        intent.putExtra(RECOMMEND_CTIME, cTime);
        intent.putExtra(RECOMMEND_DESC, desc);
        intent.putExtra(RECOMMEND_HTML, html);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_event_detail;
    }

    @Override
    protected WebDetailContract.Presenter createPresenter() {
        return new WebDetailActivityPresenter();
    }

    @Override
    public void initView() {
        super.initView();
        initConfig();
        initWebView();
        initData();
    }


    public void initData() {
        Intent intent = getIntent();
        mRecomTitle = intent.getStringExtra(RECOMMEND_TITLE);
        mRecomTime = intent.getStringExtra(RECOMMEND_CTIME);
        mRecomDesc = intent.getStringExtra(RECOMMEND_DESC);
        mRecomHtml = intent.getStringExtra(RECOMMEND_HTML);

        mToolBar.setTitleText("正在加载中...");
        mToolBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mToolBar.setTitleSelected(true);
            }
        },2000);
        mTvTitleContent.setText(mRecomTitle);
        mTvSource.setText(mRecomDesc);
        mTvTime.setText(mRecomTime);

        if (!TextUtils.isEmpty(mRecomHtml)) {
            mWebView.loadUrl(mRecomHtml);
        }
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPresenter.loadFinish();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

        });

        mWebView.addJavascriptInterface(new JsCallAndroid() {
            @JavascriptInterface
            @Override
            public void showActivity(String clazz) {
//                int userId = (int) SPUtil.getParam(BConstants.USER_ID, 0);
//                if (userId == 0) {
//                    Toast.makeText(WebDetailActivity.this, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent();
//                intent.setClassName(getPackageName(), getPackageName() + "." + clazz);
//                startActivity(intent);
//                finish();
            }
        }, "button");

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolBar.setTitleText(title);
            }
        });
    }


    public interface JsCallAndroid {
        public void showActivity(String clazz);
    }


    private void initConfig() {

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");

        //自适应屏幕
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);


        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        if (isConnected) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cacheWebView";
        mWebSettings.setAppCachePath(cacheDirPath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
