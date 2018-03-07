package com.yhp.fieenews.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.yhp.fieenews.ui.fragment.HotTopicFragment;
import com.yhp.fieenews.ui.fragment.MoreFragment;
import com.yhp.fieenews.ui.fragment.NewsFragment;
import com.yhp.fieenews.ui.fragment.TechNewsFragment;

import java.lang.ref.WeakReference;

import me.yokeyword.fragmentation.SupportFragment;


public class MyFragmentAdapter extends FragmentPagerAdapter {

    private static int ITEM_COUNT = 4;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HotTopicFragment.newInstance();
            case 1:
                return NewsFragment.newInstance();
            case 2:
                return TechNewsFragment.newInstance();
            case 3:
                return MoreFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }
}
