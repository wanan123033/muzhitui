package base.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import base.UIUtils;


/**
 * Created by gk on 2016/4/1.
 */
public class MyTabLayout extends TabLayout {
    private int i = 0;

    public MyTabLayout(Context context) {
        this(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout linearLayout = (LinearLayout) this.getChildAt(0);
        linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.shape_vertical_line));
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);


    }

    public void opt() {
        LinearLayout linearLayout = (LinearLayout) this.getChildAt(0);
        View view = linearLayout.getChildAt(0);
        linearLayout.removeView(view);
        Class class1 = linearLayout.getClass();
        Logger.w(linearLayout.toString());
        Logger.w(linearLayout.getChildCount() + "  ");
        requestLayout();
       LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = 20;
        layoutParams.rightMargin = 20;
        view.setLayoutParams(layoutParams);

        try {
            Method method = class1.getDeclaredMethod("setIndicatorPosition", int.class, int.class);
            method.setAccessible(true);
            method.invoke(linearLayout, 20, 20);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Logger.w("NoSuchMethodException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Logger.w("InvocationTargetException");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Logger.w("IllegalAccessException");
        }
    }

   /* @NonNull
    @Override
    public Tab newTab() {
        Tab tab = super.newTab();
        View view = UIUtils.inflate(getContext(), R.layout.tab_main);
        RadioButton btMaterialOuer = (RadioButton) view.findViewById(R.id.btMaterialOuer);

        tab.setCustomView(view);
        Drawable drawable = null;
        switch (i) {
            case 0:
                drawable = getResources().getDrawable(R.drawable.selector_left_material);
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.selector_center_material);
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.selector_right_material);
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btMaterialOuer.setCompoundDrawables(drawable, null, null, null);

        i++;
        return tab;
    }*/
}
