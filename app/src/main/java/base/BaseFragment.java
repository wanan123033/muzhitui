package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * <p/>
 * create an instance of this fragment.
 */
public abstract class BaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    static final String ARG_PARAM2 = "param2";
    public String mServerEror = "服务器连接失败";
    public String mNetWorkError = "请检查网络";
    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;


    FrameLayout mFlytFragmentContent;

    View mErrorView;
    View mLoadingView;
    View mEmptyView;
    View mSuccessView;
    FrameLayout.LayoutParams mBaseLayoutParams;
    protected BaseActivityTwoV mBaseActivityTwoV;
    private Snackbar mSnackbar;

    public BaseFragment() {
        // Required empty public constructor
    }

    /**
     * 函数的集合
     */
    protected Functions mFunctions;

    /**
     * activity调用此方法进行设置Functions
     *
     * @param functions
     */
    public void setFunctions(Functions functions) {
        this.mFunctions = functions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getFragmentArguments(getArguments());
        }
    }

    public void getFragmentArguments(Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.i("onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        mFlytFragmentContent = (FrameLayout) view.findViewById(R.id.flytFragmentContent);
        mBaseLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        int successLayoutId = createSuccessView();
        mSuccessView = UIUtils.inflate(getActivity(), successLayoutId);
        ButterKnife.bind(this, mSuccessView);
        mSuccessView.setLayoutParams(mBaseLayoutParams);
        if (null != mFlytFragmentContent) {
            mFlytFragmentContent.addView(mSuccessView);
        }
        return view;
    }

    public abstract int createSuccessView();

    @Override
    public void onAttach(Context context) {
        LogUtil.i("onAttach");
        super.onAttach(context);
        if (context instanceof BaseActivityTwoV) {
            mBaseActivityTwoV = (BaseActivityTwoV) context;
            mBaseActivityTwoV.setFunctionsForFragment(getTag());
        }
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(context);
    }

    @Override
    public void onDetach() {
        LogUtil.i("onAttach");
        super.onDetach();

    }

    public void showTest(String text, View view) {
        mSnackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(mSnackbar, Color.WHITE);
        mSnackbar.show();
    }

    public void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }


    public void showTest(String text) {
        mSnackbar = Snackbar.make(mFlytFragmentContent, text, Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(mSnackbar, Color.WHITE);
        mSnackbar.show();
    }

    public void showErrorView() {
        if (mErrorView == null) {
            mErrorView = UIUtils.inflate(getActivity(), R.layout.layout_error);
            mErrorView.setLayoutParams(mBaseLayoutParams);
        }
        removeLoadingView();
        mFlytFragmentContent.addView(mErrorView);
    }
    public void removeErrorView() {
        if (mErrorView!= null) {
            mFlytFragmentContent.removeView(mErrorView);
        }
        removeLoadingView();

    }
    private boolean mIsOpenLog = true;

    public void l(String log) {
        if (mIsOpenLog) {
            Logger.t(this.getClass().getSimpleName() + "1").i(log);
            // Log.i(this.getClass().getSimpleName() + "1", log);
        }
    }

    private boolean mIsAnimate;

    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = UIUtils.inflate(getActivity(), R.layout.layout_loading);
            mIvLoading = (ImageView) mLoadingView.findViewById(R.id.ivLoading);
            mLoadingView.setLayoutParams(mBaseLayoutParams);
        }
        if (!mIsAnimate) {
            mIsAnimate = true;
            mOperatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            mOperatingAnim.setInterpolator(lin);
            mIvLoading.startAnimation(mOperatingAnim);
        }
        if (mLoadingView.getParent() == null) {
         //   ((ViewGroup) mLoadingView.getParent()).removeView(mLoadingView);
            mFlytFragmentContent.addView(mLoadingView);
        }

    }

    public void removeLoadingView() {
        mFlytFragmentContent.removeView(mLoadingView);
        if (mIvLoading != null) {
            mIsAnimate = false;
            mIvLoading.clearAnimation();
        }
    }


    ImageView mIvLoading;
    private Animation mOperatingAnim;

    public void showEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = UIUtils.inflate(getActivity(), R.layout.layout_empty);
            mEmptyView.setLayoutParams(mBaseLayoutParams);
        }
        mFlytFragmentContent.addView(mEmptyView);
    }

    public void baseStartActivity(Class intentClass) {
        Intent intent = new Intent(getActivity(), intentClass);
        getActivity().startActivity(intent);
    }

    public void baseStartActivity(Class intentClass,Bundle bundle) {
        Intent intent = new Intent(getActivity(), intentClass);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Observable wrapObserverWithHttp(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
