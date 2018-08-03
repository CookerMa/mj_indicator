package com.nick.indicator.lib;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *  TODO viewpager 绑定及其他
 */
public class PageIndicator extends HorizontalScrollView {

    private int visableItemCount = 5;
    private int itemWidth;
    private List<String> mTitles = new ArrayList<>();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTabLineColor = Color.BLUE;
    private Toast mToast;
    private int mTextColor;
    private int mTabWidth;
    private int mStartCentreX;
    private int mTabHeight = 10;
    private LineStyle mStyle = LineStyle.line;
    private float mTranslationX;
    private ViewPager mViewPager;
    private Class mViewType;
    /**
     * 点击的position
     */
    private int mSelectPostion = 0;
    private int mItemHeight;
    /**
     * 还没点击前的position
     */
    private int mCurrentPostion = 0;

    private void show(String msg) {
        if (mToast == null) {
            mToast = new Toast(getContext());
        }
        mToast.setText(msg);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemWidth = getScreenWidth() / visableItemCount;
        mTabWidth = itemWidth;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mTabHeight);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        switch (LineStyle.line) {
            case line:
                float x = mStartCentreX - mTabWidth / 2 + mTranslationX;
                float x1 = mStartCentreX + mTabWidth / 2 + mTranslationX;
                canvas.drawLine(x, mItemHeight, x1, mItemHeight, mPaint);
                break;
            case rect:
                break;
            case circle:
                break;
            case rounded:
                break;
            case other:
                break;
        }
    }

    private void creatItemView() throws Exception {
        if (mTitles.isEmpty()) {
            show("must set up data");
            return;
        }
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        int width = mTitles.size() * itemWidth;

        layout.setLayoutParams(new LinearLayout.LayoutParams(width, -1));

        if (mViewType == null) {
            mViewType = TextView.class;
        }

        for (int i = 0; i < mTitles.size(); i++) {
            final int j = i;
            View itemView = (View) mViewType.getConstructor(Context.class).newInstance(getContext());
            if (itemView instanceof TextView) {
                ((TextView) itemView).setTextSize(16);
                ((TextView) itemView).setGravity(Gravity.CENTER);
                ((TextView) itemView).setText(mTitles.get(i));
            }
            //todo  其他类型自己操作，通过点击事件
            itemView.setOnClickListener(view -> {
                mCurrentPostion = mSelectPostion;
                if (mSelectPostion == j) return;
                mSelectPostion = j;
                addAnimation();
                if (mClickListener != null) {
                    mClickListener.onTabSelected(j, view);
                }
            });
            itemView.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, -1));
            layout.addView(itemView);
        }
        addView(layout);
    }

    private void addAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                if (mSelectPostion < mCurrentPostion) { //左滑
                    mTranslationX = itemWidth * (mCurrentPostion) * (1 - value)
                            + itemWidth * mSelectPostion * value;
                } else {
                    mTranslationX = itemWidth * mCurrentPostion * (1 - value)
                            + itemWidth * mSelectPostion * value;
                }
                invalidate();
            }
        });
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
//        viewPager.addOnPageChangeListener((IPageListener)pos->);
    }

    public void setTabWidth(int tabWidth) {
        if (tabWidth > itemWidth) tabWidth = itemWidth;
        mTabWidth = tabWidth;
    }

    public void setStyle(LineStyle style) {
        mStyle = style;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setTabLineColor(int tabLineColor) {
        this.mTabLineColor = tabLineColor;
    }

    public void setTitles(List<String> titles) {
        mTitles.addAll(titles);
        try {
            creatItemView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartCentreX = itemWidth / 2;
        mItemHeight = h;
    }

    public void setViewType(Class viewType) {
        mViewType = viewType;
    }

    public void setTabHeight(int tabHeight) {
        mTabHeight = tabHeight;
    }

    /**
     * 设置可见的参数
     *
     * @param visableItemCount
     */
    public void setVisableItemCount(int visableItemCount) {
        this.visableItemCount = visableItemCount;
        itemWidth = getScreenWidth() / visableItemCount;
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    //底部样式
    private enum LineStyle {
        line, rect, rounded, circle, other
    }

    private IPageIndicatorClickListener mClickListener;

    public void setClickListener(IPageIndicatorClickListener clickListener) {
        mClickListener = clickListener;
    }
}
