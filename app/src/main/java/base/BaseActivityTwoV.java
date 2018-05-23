package base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import base.com.jaeger.library.StatusBarUtil;
import base.view.DragPointView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseActivityTwoV extends AppCompatActivity {
    RelativeLayout mToolbar;
    FrameLayout mFlytContent;
    View mErrorView;
    View mLoadingView;
    View mEmptyView;
    ImageView iv_back;
    public View mSuccessView;
    FrameLayout.LayoutParams mBaseLayoutParams;
    TextView mTvTitle;
    TextView mTvRight;
    ImageView mIvRight;
    private CompositeSubscription mCompositeSubscription;
    public String mServerEror = "服务器连接失败";
    public String mNetWorkError = "请检查网络";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler.getInstance().init(this);
        setContentView(R.layout.activity_base_activity_two_v);
        setStatusBar();
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
         /*   window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);*/
            window.setStatusBarColor(Color.parseColor("#2D2C35"));
        }

        iv_back = findViewById(R.id.iv_back);
        mTvRight = findViewById(R.id.tvRight);
        mToolbar = findViewById(R.id.toolbar);
        mTvTitle = findViewById(R.id.tvTitle);
        mTvRight = findViewById(R.id.tvRight);
        mIvRight = findViewById(R.id.ivRight);
        mFlytContent = findViewById(R.id.flytContent);
        if (isFullScreen()) {
            mFlytContent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
//        setSupportActionBar(mToolbar);
        mBaseLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        int successLayoutId = createSuccessView();

        mSuccessView = UIUtils.inflate(this, successLayoutId); //可以试一下inflate加入父布局
        ButterKnife.bind(this, mSuccessView);
        setTitle("");
        init();
        mSuccessView.setLayoutParams(mBaseLayoutParams);
        if (null != mFlytContent) {
            mFlytContent.addView(mSuccessView);
        }


    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
    }

    public View getmSuccessView() {
        return mSuccessView;
    }

    public void setMyTitle(String title) {
        mTvTitle.setText(title);
    }

//    public void setBackColor(String color) {
//        mToolbar.getNavigationIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
//
//    }

    public void setTitleColor(String color) {
        mTvTitle.setTextColor(Color.parseColor(color));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3

        return super.dispatchTouchEvent(event);
    }

    public TextView showRight(String text, View.OnClickListener onClickListener) {
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(text);
        mTvRight.setOnClickListener(onClickListener);
        return mTvRight;
    }

    public ImageView showRight(int drawableId, View.OnClickListener onClickListener) {
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setImageResource(drawableId);
        mIvRight.setOnClickListener(onClickListener);
        return mIvRight;
    }

    public void setTitleBackGround(int drawableId) {
        mToolbar.setBackgroundResource(drawableId);
    }

    public TextView showRight() {
        mTvRight.setVisibility(View.VISIBLE);
        return mTvRight;
    }

    public void showBack() {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });

    }
    public void showBack(boolean flag) {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webViewShowBack()) {
                    closeKeyboard();
                    finish();
                    onBackPressed();
                }
            }
        });

    }

    protected boolean webViewShowBack() {
        return true;
    }

    public void showBack(final View.OnClickListener clickListener) {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });

    }

    public void showBack(int resId) {
        iv_back.setBackgroundResource(resId);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }


    public boolean isFullScreen() {
        return false;
    }

    public abstract void init();

    public void showSoftInput(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            if (this.getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(),
                        0); // InputMethodManager.HIDE_NOT_ALWAYS
        }
    }

    public void hideActionBar() {
        mToolbar.setVisibility(View.GONE);
    }

    public void showActionBar() {
        mToolbar.setVisibility(View.VISIBLE);

    }
    public void removeErrorView() {
        if (mErrorView!= null) {
            mFlytContent.removeView(mErrorView);
        }
        removeLoadingView();

    }
    public void showErrorView() {
        if (mErrorView == null) {
            mErrorView = UIUtils.inflate(this, R.layout.layout_error);
            mErrorView.setLayoutParams(mBaseLayoutParams);
        }
        mFlytContent.addView(mErrorView);
    }

    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = UIUtils.inflate(this, R.layout.layout_loading);
            mLoadingView.setLayoutParams(mBaseLayoutParams);
        }
        mFlytContent.addView(mLoadingView);
    }

    public void removeLoadingView() {
        mFlytContent.removeView(mLoadingView);
    }

    public View showEmptyView(int layoutId) {
        if (layoutId != 0) {
            mEmptyView = UIUtils.inflate(this, layoutId);
        } else {
            mEmptyView = UIUtils.inflate(this, R.layout.layout_empty);
        }
        mEmptyView.setLayoutParams(mBaseLayoutParams);
        mFlytContent.addView(mEmptyView);
        return mEmptyView;
    }

    public abstract int createSuccessView();

    /**
     * 为fragment设置functions，具体实现子类来做
     *
     * @param fragmentTag
     */
    public void setFunctionsForFragment(String fragmentTag) {
    }

    private Snackbar mSnackbar;

    public void showTest(String text) {
        showTest(text,Snackbar.LENGTH_SHORT);
    }
    public void showTest(String text,int duration) {
        mSnackbar = Snackbar.make(mFlytContent, text, duration);
        setSnackbarMessageTextColor(mSnackbar, Color.WHITE);
        mSnackbar.show();
    }

    public void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }


    public void showTest(View view, String text, int time, String action, View.OnClickListener onClickListener) {
        mSnackbar = Snackbar.make(view, text, time).setAction(action, onClickListener);
        setSnackbarMessageTextColor(mSnackbar, Color.WHITE);
        mSnackbar.show();
    }


    private boolean mIsOpenLog = true;

    public void l(String log) {
        if (mIsOpenLog) {
            Logger.t(this.getClass().getSimpleName() + "1").i(log);
            // Log.i(this.getClass().getSimpleName() + "1", log);
        }
    }

    public void baseStartActivity(Class intentClass) {
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
    }

    public void baseStartActivity(Class intentClass,Bundle bundle) {
        Intent intent = new Intent(this, intentClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    public Observable wrapObserverWithHttp(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public String getAppVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            Log.e("appversion",version);
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("appversion","沒有找到版本号");
            return "沒有找到版本号";
        }
    }
    //此方法，如果显示则隐藏，如果隐藏则显示
    public void hintKbOne() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
// 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }
    //此方法只是关闭软键盘
    public void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
