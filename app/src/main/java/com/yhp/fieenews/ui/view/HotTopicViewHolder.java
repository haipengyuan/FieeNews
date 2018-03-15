package com.yhp.fieenews.ui.view;


import android.content.Context;
import android.graphics.Color;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.yhp.fieenews.MainActivity;
import com.yhp.fieenews.R;
import com.yhp.fieenews.model.Topic;
import com.yhp.fieenews.ui.base.BaseViewHolder;
import com.yhp.fieenews.ui.fragment.HotTopicFragment;
import com.yhp.fieenews.ui.fragment.InstantReadFragment;
import com.yhp.fieenews.ui.fragment.MainFragment;
import com.yhp.fieenews.ui.fragment.TopicDetailFragment;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportActivity;


public class HotTopicViewHolder extends BaseViewHolder<Topic> {

    @BindView(R.id.txt_topic_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_topic_summary)
    TextView mTxtSummary;
    @BindView(R.id.txt_instant_read)
    TextView mTxtInstantRead;
    @BindView(R.id.divider_instant_read)
    View mDividerInstantRead;
    @BindView(R.id.frame_instant_read)
    FrameLayout mFrameInstantRead;

    private Topic mTopic;

    public HotTopicViewHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.list_item_topic);
    }

    @Override
    public void bindTo(final Topic value) {
        mTopic = value;

        // 设置文本样式
        SpannableString spannableString =
                new SpannableString(value.getTitle() + "  " + value.getPublishDateCountDown());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#AAACB4")),
                value.getTitle().length() + 2,
                value.getTitle().length() + value.getPublishDateCountDown().length() + 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), value.getTitle().length() + 2,
                value.getTitle().length() + value.getPublishDateCountDown().length() + 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTxtTitle.setText(spannableString);
        mTxtSummary.setText(value.getSummary());

        mTxtSummary.setVisibility(TextUtils.isEmpty(value.getSummary()) ? View.GONE : View.VISIBLE);
        mTxtInstantRead.setVisibility(mTopic.hasInstantView() ? View.VISIBLE : View.GONE);
        mDividerInstantRead.setVisibility(mTopic.hasInstantView() ? View.VISIBLE : View.GONE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) view.getContext()).findFragment(MainFragment.class)
                        .start(TopicDetailFragment.newInstance(value));
            }
        });

        mFrameInstantRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstantReadFragment.newInstance(value.getId())
                        .show(((SupportActivity) view.getContext()).getSupportFragmentManager(),
                                InstantReadFragment.TAG);
            }
        });
    }
}
