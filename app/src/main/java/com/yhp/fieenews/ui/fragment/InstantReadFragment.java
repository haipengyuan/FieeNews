package com.yhp.fieenews.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.yhp.fieenews.MainActivity;
import com.yhp.fieenews.R;
import com.yhp.fieenews.app.Constant;
import com.yhp.fieenews.app.MyApplication;
import com.yhp.fieenews.model.InstantReadData;
import com.yhp.fieenews.ui.base.INetworkPresenter;
import com.yhp.fieenews.ui.base.INetworkView;
import com.yhp.fieenews.ui.presenter.InstantReadPresenter;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstantReadFragment extends DialogFragment implements INetworkView<InstantReadData> {

    public static final String TAG = "InstantReadFragment";

    @BindView(R.id.txt_topic_title)
    TextView mTxtTopicTitle;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.txt_instant_source)
    TextView mTxtSource;
    @BindView(R.id.txt_origin_site)
    TextView mTxtGoOrigin;

    private String mTopicId;
    private InstantReadPresenter mPresenter = new InstantReadPresenter(this);

    public static InstantReadFragment newInstance(String topicId) {
        InstantReadFragment fragment = new InstantReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_TOPIC_ID, topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_instant_read, container, false);
        ButterKnife.bind(this, view);
        mTopicId = getArguments().getString(Constant.BUNDLE_TOPIC_ID);
        getPresenter().getInstantRead(mTopicId);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebSetting();
    }


    @Override
    public void onSuccess(final InstantReadData instantReadData) {
        if (instantReadData == null) {
            return;
        }

        mTxtTopicTitle.setText(instantReadData.getTitle());
        mTxtSource.setText(getString(R.string.source_fromat, instantReadData.getSiteName()));
        if (!TextUtils.isEmpty(instantReadData.getUrl())) {
            mTxtGoOrigin.setVisibility(View.VISIBLE);
            mTxtGoOrigin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    ((MainActivity) getContext()).findFragment(MainFragment.class)
                            .start(WebViewFragment.newInstance(instantReadData.getUrl()));
                }
            });
        }

        String htmlHead = "<head>"
                +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<link rel=\"stylesheet\" href=\"https://unpkg.com/mobi.css/dist/mobi.min.css\">"
                + "<style>"
                + "img{max-width:100% !important; width:auto; height:auto;}"
                + "body {font-size: 110%;word-spacing:110%；}"
                + "</style>"
                + "</head>";
        String htmlContent = "<html>"
                + htmlHead
                + "<body style:'height:auto;max-width: 100%; width:auto;'>"
                + instantReadData.getContent()
                + "</body></html>";
        mWebView.loadData(htmlContent, "text/html; charset=UTF-8", null);
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(getContext(), "请求错误", Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Override
    public InstantReadPresenter getPresenter() {
        return mPresenter;
    }

    @OnClick(R.id.img_back)
    void onCloseClick() {
        dismiss();
    }

    private void initWebSetting() {
        WebSettings mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setUseWideViewPort(true);
        mWebSetting.setLoadWithOverviewMode(true);
        mWebSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        mWebSetting.setLoadsImagesAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (URLUtil.isNetworkUrl(url)) {
                    dismiss();
                    ((MainActivity) getContext()).findFragment(MainFragment.class)
                            .start(WebViewFragment.newInstance(url));
                }
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.endsWith("mobi.min.css")) {
                    //使用本地 css 优化阅读视图
                    WebResourceResponse resourceResponse = null;
                    try {
                        InputStream in = MyApplication.getContext().getAssets().open("css/mobi.css");
                        resourceResponse = new WebResourceResponse("text/css", "UTF-8", in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (resourceResponse != null) {
                        return resourceResponse;
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
    }
}
