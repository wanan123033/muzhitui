package base.behavior;

/**
 * Created by gk on 2016/2/24.
 */

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hehe on 2016/2/24.
 */
public class HorizonNarrowBehavior extends CoordinatorLayout .Behavior<TextView> {
    private int mTotal;
    private CoordinatorLayout .LayoutParams mTextlayoutParams ;
    private int mTextWidth ;
    private int narrowWidth ;
    private boolean mIsFirst = true;

    public HorizonNarrowBehavior() {
    }

    public HorizonNarrowBehavior(Context context , AttributeSet attrs) {
        super (context, attrs) ;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent , TextView child, View dependency) {
        if (mIsFirst) {
            mIsFirst = false;
            mTextlayoutParams = (CoordinatorLayout .LayoutParams) child.getLayoutParams();
            mTextWidth = mTextlayoutParams. width;
            narrowWidth = mTextlayoutParams. width;
            ((RecyclerView)dependency).addOnScrollListener( new RecyclerView.OnScrollListener() {
                private int totalDy = 0;
                @Override
                public void onScrolled (RecyclerView recyclerView , int dx, int dy) {
                    mTotal += dx ;
                    // setTranslation/Alpha here according to totalDy.
                }
            });
        }
        return dependency instanceof RecyclerView ;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent , TextView child, View dependency) {
        //super.onDependentViewChanged(parent, child, dependency);
        return false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout , TextView child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout , TextView child, View target, int dx , int dy, int[] consumed) {
        // mTotal = getScrolledDistance((RecyclerView) target);
        Log. i("wocao", "mTotal = " + mTotal + "  " + child.getWidth() + "   " + dx);
        int offset = ( mTotal)/2;
        if ( mTextWidth - offset > 40 &&dx!=0) {
            mTextlayoutParams .width = mTextWidth - offset;
            narrowWidth = mTextlayoutParams. width;
            target.setTranslationX(-offset) ;
            child.setLayoutParams( mTextlayoutParams);
        }


    }


    private int getScrolledDistance(RecyclerView view) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager() ;
        View firstVisiblleItem = view.getChildAt( 0);
        int lastItemPosition = layoutManager.findLastVisibleItemPosition() ;
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition() ;
        int itemHeight = firstVisiblleItem.getWidth() ;
        // layoutManager.getRightDecorationWidth()
        int firstItemBottom = layoutManager.getDecoratedRight(firstVisiblleItem) ;
        return (lastItemPosition + 1) * itemHeight - firstItemBottom - (lastItemPosition - firstItemPosition) * itemHeight;
    }


    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout , TextView child, View directTargetChild, View target, int nestedScrollAxes) {
        super .onNestedScrollAccepted(coordinatorLayout , child, directTargetChild, target , nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout , TextView child, View target, int dxConsumed , int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super .onNestedScroll(coordinatorLayout , child, target, dxConsumed , dyConsumed, dxUnconsumed, dyUnconsumed) ;
    }


}
