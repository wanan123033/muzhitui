package com.nevermore.muzhitui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.fragment.MyWorksModeFragment;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.bean.MyStyle;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.util.CacheUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class StartMakeActivity extends BaseActivityTwoV {


    @BindView(R.id.tvDate)
    TextView mTvDate;
    @BindView(R.id.flytDate)
    FrameLayout mFlytDate;
    @BindView(R.id.rvOtherStyle)
    RecyclerView mRvOtherStyle;
    @BindView(R.id.etPub)
    EditText etPub;
    @BindView(R.id.etUrl)
    EditText etUrl;
    @BindView(R.id.tvTip)
    TextView mTvTip;

    private CommonAdapter<MyStyle.StyListBean> mCommonAdapter;
    private List<MyStyle.StyListBean> mLt = new ArrayList<>();
    private View mSelectedView;
    TimePickerView pvTime;
    private LoadingAlertDialog mLoadingAlertDialog;
    private int mStyleId;
    private int mId;
    private MyStyle.StyListBean mStylistBean;
    private MyMode.TopArrayBean mTopArrayBean;
    private String mImg;
    private String mOutputPath;
    public static final int REQUEST_CLIP_IMAGE = 2028;

    @Override
    public void init() {
        if (getIntent() != null) {
            mTopArrayBean = (MyMode.TopArrayBean) getIntent().getSerializableExtra(MyWorksModeFragment.YEMEI);
            if (mTopArrayBean != null) {
                mId = mTopArrayBean.getTopId();
                mTvDate.setText(mTopArrayBean.getTopDate().substring(0, 10));
                etPub.setText(mTopArrayBean.getPublicNo());
                etUrl.setText(mTopArrayBean.getLinkUrl());
                mImg = mTopArrayBean.getImg();
            }
        }
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        showBack();
        setMyTitle("页眉模板制作");
        showRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadYeMei();
            }
        });
        mLt.add(new MyStyle.StyListBean());
        mCommonAdapter = new CommonAdapter<MyStyle.StyListBean>(this, R.layout.rvitem_style, mLt) {
            @Override
            public void convert(ViewHolder holder, final MyStyle.StyListBean o) {
                if (holder.getMyPosition() != 0) {
                    holder.getView(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleStyle(o);
                        }
                    });
                    if (mId == 0) {
                        if (holder.getMyPosition() == 1) {
                            mSelectedView = holder.getView(R.id.ivStyleSelect);
                            holder.setVisible(R.id.ivStyleSelect, true);
                            mImg = o.getUrl();
                        }
                    } else {
                        if (o.getUrl().equals(mImg)) {
                            mSelectedView = holder.getView(R.id.ivStyleSelect);
                            holder.setVisible(R.id.ivStyleSelect, true);
                        }
                    }
                    holder.setImageURL(R.id.ivStyle, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + o.getUrl(), false);
                } else {
                    holder.setVisible(R.id.ivClose, false);
                }

            }

        };
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTvDate.setText(getTime(date));
            }
        });
        if (mId == 0) {
            mTvDate.setText(getTime(new Date()));
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRvOtherStyle.setLayoutManager(gridLayoutManager);
        mCommonAdapter.setOnItemClickListener(new OnItemClickListener<MyStyle.StyListBean>() {

            @Override
            public void onItemClick(ViewGroup parent, View view, MyStyle.StyListBean o, int position, ViewHolder viewHolder) {
                CacheUtil.getInstance().add(CacheUtil.FORMAT_FRTE_TYPE,1);
                if (position == 0) {
                    ImageUtil.getInstance().chooseImage("开始制作",new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList != null && !resultList.isEmpty()) {
                                mImg = resultList.get(0).getPhotoPath();

                                mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                                PhotoActionHelper.clipImage(StartMakeActivity.this).input(mImg).output(mOutputPath).setExtraHeight(UIUtils.dip2px(51)).maxOutputWidth(640)
                                        .requestCode(REQUEST_CLIP_IMAGE).start();
                            }
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {

                        }
                    }, 1);
                } else {
                    if (mSelectedView == null) {
                        mSelectedView = viewHolder.getView(R.id.ivStyleSelect);
                    } else {
                        mSelectedView.setVisibility(View.GONE);
                        mSelectedView = viewHolder.getView(R.id.ivStyleSelect);
                    }
                    mImg = o.getUrl();
                    viewHolder.setVisible(R.id.ivStyleSelect, true);
                    mStylistBean = o;
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, MyStyle.StyListBean o, int position, ViewHolder viewHolder) {

                return false;
            }
        });
        mRvOtherStyle.setAdapter(mCommonAdapter);
        loadStyle();
        mTvTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseStartActivity(TipActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && data != null
                && (requestCode == REQUEST_CLIP_IMAGE)) {
            String path = PhotoActionHelper.getOutputPath(data);
            if (path != null) {
               /* Bitmap bitmap = BitmapFactory.decodeFile(path);
                mIvHead.setImageBitmap(bitmap); // 这里注意 别内存溢出*/
                final Uri uri = Uri.parse("file:/" + path);
                mImg = path;
                uploadStyle(mImg);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void uploadStyle(String img) {
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().addSty((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),ImageUtil.getInstance().wrapUploadImgRequest(new File(img)))).subscribe(new Subscriber<MyStyle.StyListBean>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(MyStyle.StyListBean myStyle) {
                if ("1".equals(myStyle.getState())) {
                    mCommonAdapter.addDate(myStyle);
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    /**
     * 保存或修改页眉模板
     */
    private void uploadYeMei() {
        String topData = mTvDate.getText().toString();
        String publicNo = etPub.getText().toString();
        String url = etUrl.getText().toString();
        if (TextUtils.isEmpty(topData)) {
            showTest("请选择日期");
            return;
        }
        if (TextUtils.isEmpty(publicNo)) {
            showTest("请填写公众号名称");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            url = "http://www.tpy10.net/create.php?name=muzhitui8";
        }
        if (TextUtils.isEmpty(mImg)) {
            showTest("请选择样式");
            return;
        }
        mLoadingAlertDialog.show();
        Observable observable = null;
        if (mId == 0) {
            observable = wrapObserverWithHttp(WorkService.getWorkService().saveTop((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),publicNo, url, topData, mImg));
        } else {
            observable = wrapObserverWithHttp(WorkService.getWorkService().updateTop((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),mId, publicNo, url, topData, mImg));
        }

        Subscription sbMyAccount = observable.subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(BaseBean myStyle) {
                if (myStyle.getState() == 1) {
                    EventBus.getDefault().post(new MyModeRefreshEvent());
                    finish();
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);
    }

    private void loadStyle() {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().findSty((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyStyle>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(MyStyle myStyle) {
                if ("1".equals(myStyle.getState())) {
                    mCommonAdapter.addDate(myStyle.getStyList());
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);

    }

    private void deleStyle(final MyStyle.StyListBean styListBean) {
        mLoadingAlertDialog.show();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleSty((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),styListBean.getId())).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(BaseBean myStyle) {
                if (myStyle.getState() == 1) {
                    mCommonAdapter.removeDate(styListBean);
                    showTest("删除成功");
                } else {
                    showTest(mServerEror);
                }

            }
        });
        addSubscription(sbMyAccount);

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_start_make;
    }


    @OnClick(R.id.flytDate)
    public void onClick() {
        pvTime.show();
    }

}
