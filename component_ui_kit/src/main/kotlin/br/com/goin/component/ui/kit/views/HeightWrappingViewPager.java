package br.com.goin.component.ui.kit.views;

import android.content.Context;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class HeightWrappingViewPager extends ViewPager {

    public HeightWrappingViewPager(Context context) {
        super(context);
    }

    public HeightWrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            height = getChildHeight(widthMeasureSpec, height);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getChildHeight(int widthMeasureSpec, int height) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(child instanceof SwipeRefreshLayout){
                child = ((SwipeRefreshLayout) child).getChildAt(0);
                if("CircleImageView".equals(child.getClass().getSimpleName())){
                    child = ((SwipeRefreshLayout) getChildAt(i)).getChildAt(0);
                }
            }
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        return height;
    }
}