package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.event.EditInfoEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.RichEditor;


/**
 * Created by Administrator on 2017/1/3.
 * 原创文章编辑，修改标题
 */

public class EditInfoActivity extends Activity implements View.OnLayoutChangeListener {
    RichEditor mEditor;

    private ImageButton button_textColor;
    private ImageButton button_bgColor;
    private int flag;
    private String state;
    private TextView mTvEditInfoTitle;
    private String text;
    private int texttype;

    private TextView mPreview;
    private TextView mTxtView;
    private HorizontalScrollView hlv_css;

    private int screenHeight,keyHeight;
    Toolbar mToolbar;

    public static final String PUBLIC_TITLE = "EditInfoTitle";  //界面标题
    public static final String TITLE_NAME = "titleName";
    public static final String textType = "textType";   //修改类型：修改的是编辑区还是视频模板上面的东西

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(300);
        mEditor.setEditorFontSize(17);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10,10,10,10);
        mEditor.setEditorHeight(200);
        mTxtView = (TextView) findViewById(R.id.tv_previewtxt);
        hlv_css = (HorizontalScrollView) findViewById(R.id.hlv_css);
        mTvEditInfoTitle = (TextView)findViewById(R.id.tvTitle);
        mPreview = (TextView) findViewById(R.id.tv_previewHtml);
        initUIView();
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                String stext = text.replaceAll("<(.|\n)*?>","").replaceAll("\\&[a-zA-Z]{1,10};"," ");
                if (flag == 101) {
                    if(stext.length() > 25){
                        stext = stext.substring(0,25);
                        mEditor.setHtml(stext);
                        mEditor.focusEditor();
                    }
                }else if(flag == 102){
                    if(stext.length() > 2000){
                        mEditor.setHtml(mPreview.getText().toString());
                        mEditor.focusEditor();
                    }
                }
                mPreview.setText(text);
                mTxtView.setText(stext);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setHeading(6);
            }
        });
        button_textColor = (ImageButton)findViewById(R.id.action_txt_color);

        button_textColor.setOnClickListener(new View.OnClickListener() {


            @Override public void onClick(View v) {
                showColorPopwindow(1);
            }
        });
        button_bgColor = (ImageButton) findViewById(R.id.action_bg_color);
        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                showColorPopwindow(2);
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            int color;
            @Override public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(color == Color.BLACK){
                    button.setColorFilter(Color.BLUE);
                    color = Color.BLUE;
                }else{
                    button.setColorFilter(Color.BLACK);
                    color = Color.BLACK;
                }
                mEditor.setAlignRight();
            }
        });


        findViewById(R.id.tvRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mPreview.getText().toString();
                EditInfoEvent info = new EditInfoEvent(flag, text, state);
                if(texttype != -1) {
                    info.setTextType(texttype);
                }
                info.setText(mTxtView.getText().toString());
                EventBus.getDefault().post(info);
                finish();
            }
        });

        showBack();


    }

    private void initUIView() {
        flag = getIntent().getIntExtra(PUBLIC_TITLE, 102);
        state = getIntent().getStringExtra("textState");
        if (flag == 101) {
            mTvEditInfoTitle.setText("修改标题");
            text = getIntent().getStringExtra(TITLE_NAME);
            mEditor.setPlaceholder("请输入文字...(字数限制25字以内)");
        }else {
            mTvEditInfoTitle.setText("添加文字");
            text = getIntent().getStringExtra("text");
            texttype = getIntent().getIntExtra(textType,-1);
            mEditor.setPlaceholder("请输入文字...(字数限制2000字以内)");
        }
        mEditor.setTextColor(Color.BLACK);
        if(!TextUtils.isEmpty(text)){
            mEditor.setHtml(text);
            mPreview.setText(text);
            mTxtView.setText(text.replaceAll("<(.|\n)*?>",""));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mEditor != null) {
            mEditor.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEditor != null) {
            mEditor.resumeTimers();
        }
        findViewById(R.id.ll_rootView).addOnLayoutChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        mEditor.destroy();
        super.onDestroy();
    }
    private void showColorPopwindow(final int type){
        findViewById(R.id.color_list).setVisibility(View.VISIBLE);
        RecyclerView mList = (RecyclerView)findViewById(R.id.color_list);
        String[] strColor = {"#000000", "#ffffff", "#545454", "#a8a8a8", "#3fe3c5", "#00ac97", "#009a7b", "#0c9564", "#7dc143", "#b8dc88", "#f8e100", "#dbb40a", "#b18302", "#a2711f", "#cc7020", "#a3701f", "#cc6f21", "#f97f2c", "#fc552b", "#ee332a", "#f48ebd", "#f075ab", "#fd43a8", "#ca28af", "#9521c0", "#7748f6", "#6465fe", "#2f47b1", "#0161b8", "#017cbf", "#69adde"};
        List mLtObject = Arrays.asList(strColor);
        CommonAdapter mAdapter = new CommonAdapter<String>(this, R.layout.rvitem_textcolor, mLtObject) {
            @Override
            public void convert(ViewHolder holder, String o) {
                holder.setBackgroundColor(R.id.view, Color.parseColor(o));
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                findViewById(R.id.color_list).setVisibility(View.GONE);
                if(type == 1){
                    mEditor.setTextColor(Color.parseColor(String.valueOf(o)));
                }else if(type ==2){
                    mEditor.setTextBackgroundColor(Color.parseColor(String.valueOf(o)));
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            Log.i("TAG","监听到软键盘弹起...");
            String text = mTvEditInfoTitle.getText().toString().trim();
            if(text.equals("修改标题")){
                hlv_css.setVisibility(View.GONE);
            }else {
                hlv_css.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.color_list).setVisibility(View.GONE);
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            Log.i("TAG","监听到软键盘关闭...");
            hlv_css.setVisibility(View.GONE);
            findViewById(R.id.color_list).setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,8.0f);
            mEditor.setLayoutParams(params);

        }
    }

    public void showBack() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.getNavigationIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });

    }
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
