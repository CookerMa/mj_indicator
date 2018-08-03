package com.nick.indicator.lib;

import android.support.v4.view.ViewPager;


@FunctionalInterface
public interface IPageListener extends ViewPager.OnPageChangeListener {
    @Override
    default void onPageScrollStateChanged(int state){};

    @Override
    void onPageSelected(int position);

    @Override
    default void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){};
}
