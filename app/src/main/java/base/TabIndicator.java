package base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gk on 2016/2/7.
 */
public class TabIndicator extends View {
    private static final int RADIU = 4;
    private static final int INTERVAL = 5;
    private static final String DEFAULTCOLOR = "#efefef";
    private static final String SELECTORCOLOR = "#d83543";
    private int mCurrrent;
    private int mSize;
    private int mRadius;
    private int mInterval;
    private Paint mDefaultPaint;
    private Paint mSelectPaint;

    public TabIndicator(Context context) {
        super(context);
        init();
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRadius = UIUtils.dip2px(RADIU);
        mInterval = UIUtils.dip2px(INTERVAL);
        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setStyle(Paint.Style.FILL);
        mDefaultPaint.setColor(Color.parseColor(DEFAULTCOLOR));
        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(Color.parseColor(SELECTORCOLOR));
        mSelectPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mSize * mRadius * 2 + (mSize - 1) * mInterval, mRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mSize; i++) {
            if (i != mCurrrent) {
                canvas.drawCircle(mRadius + i * mRadius * 2 + mInterval * i, mRadius, mRadius, mDefaultPaint);
            } else {
                canvas.drawCircle(mRadius + i * mRadius * 2 + mInterval * i, mRadius, mRadius, mSelectPaint);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        final PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrrent = position;
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSize = adapter.getCount();
        invalidate();
    }
}
