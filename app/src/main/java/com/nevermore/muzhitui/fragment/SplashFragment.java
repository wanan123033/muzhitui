package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nevermore.muzhitui.LoginActivity;
import com.nevermore.muzhitui.R;

import base.BaseFragment;
import base.SPUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hehe on 2016/5/21.
 */
public class SplashFragment extends BaseFragment {
    public static final String KEY_POSITION = "POSITION";
    @BindView(R.id.btEnter)
    Button mBtEnter;
    @BindView(R.id.ivSplash)
    ImageView mIvSplash;
    private int mPosition;

    @Override
    public void getFragmentArguments(Bundle bundle) {
        super.getFragmentArguments(bundle);
        mPosition = bundle.getInt(KEY_POSITION);
    }

    @Override
    public int createSuccessView() {
        return R.layout.fragment_splash;
    }


    public static SplashFragment newInstance(int position) {
        SplashFragment splashFragment = new SplashFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        splashFragment.setArguments(bundle);
        return splashFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (mPosition) {
            case 0:
                mIvSplash.setImageResource(R.drawable.ic_splash1);
                break;
            case 1:
                mIvSplash.setImageResource(R.drawable.ic_splash2);
                break;
            case 2:
                mIvSplash.setImageResource(R.drawable.ic_splash3);
                break;
            default:
                mIvSplash.setImageResource(R.drawable.ic_splash4);
                mBtEnter.setVisibility(View.VISIBLE);
                break;
        }

    }

    @OnClick(R.id.btEnter)
    public void onClick() {
        SPUtils.put(SPUtils.KEY_ISFIRSTENTER, false);
        baseStartActivity(LoginActivity.class);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
