package com.yhp.fieenews.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yhp.fieenews.app.Constant;
import com.yhp.fieenews.model.Topic;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import com.yhp.fieenews.R;

public class WebViewFragment extends SupportFragment {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.txt_toolbar_title)
    TextView mTxtTitle;
    @BindView(R.id.progress_bar_loading_web)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.web_view_article_page)
    WebView mWebView;

    private Topic mTopic;
    private String mUrl;

    public static WebViewFragment newInstance(Topic topic) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_TOPIC, Parcels.wrap(topic));
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_page, container, false);
        ButterKnife.bind(this, view);
        mTopic = Parcels.unwrap(getArguments().getParcelable(Constant.EXTRA_TOPIC));
        mUrl = getArguments().getString(Constant.EXTRA_URL);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initWebView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.stopLoading();
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 按下返回键后退网页
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onBackPressedSupport();
    }

    private void initView() {
        // 下拉刷新
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                mWebView.reload();
            }
        });

        // 设置导航图标
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_close);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });

        // TODO toolbar menu
    }

    private void initWebView() {
        initWebSettings();

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient() {
            // 获得网页的加载进度并显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }

            // 获取网页的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTxtTitle.setText(title);
            }
        });

        mWebView.loadUrl(TextUtils.isEmpty(mUrl) ? mTopic.getUrl() : mUrl);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initWebSettings() {
        WebSettings webSettings = mWebView.getSettings();
        // 设置webView支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 设置自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        // 优先加载本地缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath);
        // webSettings.setDatabasePath(cacheDirPath);

        // 缩放操作
        // 支持缩放，默认为true
        webSettings.setSupportZoom(true);
        // 设置内置的缩放控件，若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        // 隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        // 设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.supportMultipleWindows();
        webSettings.setNeedInitialFocus(true);
    }

}
