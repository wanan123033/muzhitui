package base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MenuHorView extends RelativeLayout {
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;

    public MenuHorView(Context context) {
        super(context);
        initView(context,null);
    }

    public MenuHorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public MenuHorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_menu_hor,this,false);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MenuHorView);
        ButterKnife.bind(this,this);
        iv_icon.setImageResource(array.getResourceId(R.styleable.MenuHorView_image,0));
        tv_title.setText(array.getText(R.styleable.MenuHorView_text));
        array.recycle();
    }
}
