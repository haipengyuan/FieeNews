package com.yhp.fieenews.model;


import android.text.TextUtils;

import com.yhp.fieenews.util.TimeUtil;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Topic {
    String id;
    String order;
    String title;
    String siteName;
    String authorName;
    String url;
    String mobileUrl;
    String summary;
    ArrayList<Topic> newsArray;
    public String publishDate;
    Extra extra;

    public Topic() {
    }

    public String getSiteName() {
        return siteName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getUrl() {
        if (!TextUtils.isEmpty(mobileUrl)) return mobileUrl;
        return url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        if (title != null) {
            return title.trim();
        } else {
            return "";
        }
    }

    public String getSummary() {
        if (summary != null) {
            return summary.trim();
        } else {
            return "";
        }
    }

    public List<Topic> getNewsArray() {
        return newsArray;
    }

    public String getPublishDateCountDown() {
        return TimeUtil.countDown(publishDate);
    }

    public String getFormatPublishDate() {
        return TimeUtil.getFormatDate(publishDate);
    }

    public String getOrder() {
        return order;
    }

    public String getLastCursor() {
        if (TextUtils.isEmpty(order)) {
            return String.valueOf(TimeUtil.getTimeStamp(publishDate));
        } else {
            return order;
        }
    }

    public boolean hasInstantView() {
        if (extra != null) return extra.instantView;
        return false;
    }

    @Parcel
    public static class Extra {
        boolean instantView;
    }
}
