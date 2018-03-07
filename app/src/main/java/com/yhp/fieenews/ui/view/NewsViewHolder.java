package com.yhp.fieenews.ui.view;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yhp.fieenews.R;
import com.yhp.fieenews.app.MyApplication;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.ui.base.BaseViewHolder;
import com.yhp.fieenews.ui.fragment.MainFragment;
import com.yhp.fieenews.ui.fragment.WebViewFragment;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportActivity;

public class NewsViewHolder extends BaseViewHolder<Topic> {

    @BindView(R.id.txt_news_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_news_summary)
    TextView mTxtSummary;
    @BindView(R.id.txt_news_time)
    TextView mTxtTime;

    public NewsViewHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.list_item_news);
    }

    @Override
    public void bindTo(final Topic value) {
        mTxtTitle.setText(value.getTitle());
        mTxtSummary.setText(value.getSummary());

        String time;
        if ((!TextUtils.isEmpty(value.getAuthorName())) && (!TextUtils.isEmpty(value.getSiteName()))) {
            time = MyApplication.getContext().getString(R.string.site_author_time_format,
                    value.getSiteName(), value.getAuthorName(), value.getPublishDateCountDown());
        } else if (TextUtils.isEmpty(value.getAuthorName()) && TextUtils.isEmpty(value.getSiteName())) {
            time = value.getPublishDateCountDown();
        } else if (TextUtils.isEmpty(value.getSiteName())) {
            time = MyApplication.getContext().getString(R.string.author_time_format,
                    value.getAuthorName(), value.getPublishDateCountDown());
        } else {
            time = MyApplication.getContext().getString(R.string.author_time_format,
                    value.getSiteName(), value.getPublishDateCountDown());
        }
        mTxtTime.setText(time);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SupportActivity) view.getContext()).findFragment(MainFragment.class)
                        .start(WebViewFragment.newInstance(value));
            }
        });

        mTxtTitle.setVisibility(TextUtils.isEmpty(value.getTitle()) ? View.GONE : View.VISIBLE);
        mTxtSummary.setVisibility(TextUtils.isEmpty(value.getSummary()) ? View.GONE : View.VISIBLE);
    }
}
