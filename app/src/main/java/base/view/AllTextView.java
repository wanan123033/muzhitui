package base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/11.
 * 可以显示全部内容或者部分内容，有全部、收起的效果
 */

public class AllTextView extends LinearLayout {

    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_allContent)
    TextView tv_allContent;
    @BindView(R.id.tv_all)
    TextView tv_all;
    private int length;

    public AllTextView(Context context) {
        super(context);
        initView(null);
    }

    public AllTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public AllTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.textview_all,this,true);
        TypedArray array = getContext().obtainStyledAttributes(attr,R.styleable.AllTextView);
        length = array.getInt(R.styleable.AllTextView_text_length,66);
        float textSize = array.getDimension(R.styleable.AllTextView_textSize, 14);
        int color = array.getColor(R.styleable.AllTextView_textColor, Color.parseColor("#393737"));
        array.recycle();
        ButterKnife.bind(this);
        setTextSize(textSize);
        setTextColor(color);
    }

    public void setTextSize(float textSize) {
//        tv_content.setTextSize(textSize);
//        tv_allContent.setTextSize(textSize);
//        tv_all.setTextSize(textSize);
    }

    public void setTextColor(int color) {
        tv_allContent.setTextColor(color);
        tv_content.setTextColor(color);
    }

    public void setText(String text){
        if(TextUtils.isEmpty(text)){
            setVisibility(GONE);
            return;
        }
        tv_allContent.setText(text);
        tv_all.setText("全部");
        if(text.length() > length){
            tv_all.setVisibility(VISIBLE);
            tv_content.setText(text.substring(0,length));
            tv_content.setVisibility(VISIBLE);
            tv_allContent.setVisibility(GONE);
            setVisibility(VISIBLE);
        }else{
            tv_all.setVisibility(GONE);
            tv_content.setVisibility(VISIBLE);
            tv_content.setText(text);
            tv_allContent.setVisibility(GONE);
            setVisibility(VISIBLE);
        }
    }

    public void setTextLength(int length){
        this.length = length;
    }

    public String getText(){
        return tv_allContent.getText().toString();
    }

    @OnClick(R.id.tv_all)
    public void onClick(View view){
        String text = ((TextView) view).getText().toString().trim();
        if(text.equals("全部")){
            tv_allContent.setVisibility(VISIBLE);
            tv_content.setVisibility(GONE);
            ((TextView) view).setText("收起");
        }else{
            tv_allContent.setVisibility(GONE);
            tv_content.setVisibility(VISIBLE);
            ((TextView) view).setText("全部");
        }
    }
}
