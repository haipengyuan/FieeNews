package com.yhp.fieenews.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.fieenews.R;
import com.yhp.fieenews.ui.MyFragmentAdapter;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends SupportFragment {

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;

    private BottomNavigationMenuView menuView;
    private MyFragmentAdapter myFragmentAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        // 关闭BottomNavigationView的滑动效果
        disableNavigationShiftMode();

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int currentItem = 0;
                        switch (item.getItemId()) {
                            case R.id.menu_item_hot_topic:
                                currentItem = 0;
                                break;
                            case R.id.menu_item_news:
                                currentItem = 1;
                                break;
                            case R.id.menu_item_tech_news:
                                currentItem = 2;
                                break;
                            case R.id.menu_item_more:
                                currentItem = 3;
                                break;
                        }

                        // 当前显示页面为导航栏点击位置对应页面
                        if (mViewPager.getCurrentItem() == currentItem) {
                            // TODO 刷新页面或者回到顶部
                        } else {
                            mViewPager.setCurrentItem(currentItem);
                        }
                        return true;
                    }
                });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (myFragmentAdapter == null) {
            myFragmentAdapter = new MyFragmentAdapter(getFragmentManager());
        }
        mViewPager.setAdapter(myFragmentAdapter);
    }

    /**
     * 关闭BottomNavigationView的滑动效果
     */
    private void disableNavigationShiftMode() {
        menuView = (BottomNavigationMenuView)
                mBottomNavigationView.getChildAt(0);

        try {
            // 使用反射
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView)
                        menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
