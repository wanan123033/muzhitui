package base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/1/8.
 */

public class ScrollView extends android.widget.ScrollView implements View.OnTouchListener{
    private OnScrollListener onScrollListener;
    public ScrollView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollY=view.getScrollY();
                int height=view.getHeight();
                int scrollViewMeasuredHeight=getChildAt(0).getMeasuredHeight();
                if(scrollY==0 && onScrollListener != null){
                    onScrollListener.scrollTop();
                }else if((scrollY+height)==scrollViewMeasuredHeight && onScrollListener != null){
                    onScrollListener.scrollDown();
                }
                break;
        }
        return false;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnScrollListener{

        void scrollTop();

        void scrollDown();
    }
}
