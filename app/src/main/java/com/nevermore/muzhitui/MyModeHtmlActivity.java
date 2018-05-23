package com.nevermore.muzhitui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.BaseMyMode;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.network.WorkService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.MultiItemCommonAdapter;
import base.recycler.recyclerview.MultiItemTypeSupport;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

public class MyModeHtmlActivity extends BaseActivityTwoV {

    @BindView(R.id.list)
    RecyclerView mList;
    List<BaseMyMode> mLtObject = new ArrayList<>();
    @BindView(R.id.ivAddMode)
    ImageView mIvAddMode;
    private MultiItemCommonAdapter mAdapter;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    private int mTopSize = 4;
    private int mBotSize = 8;
    private LoadingAlertDialog mLoadingAlertDialog;
    private MyMode mMyMode;
    public static final String YEMEI = "YEMEI";
    public static final String YEJIAO = "YEJIAO";
    public static final String AD = "AD";

    @Override
    public int createSuccessView() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_mywork_mode;
    }

    @Override
    public void init() {
        showBack();
        setMyTitle("我的模板");
        putData();
        loadData();
    }


    private boolean isTitle(int position) {
        if (position == 0 || position == mTopSize + 1 || position == mTopSize + 1 + mBotSize + 1) {
            return true;
        } else {
            return false;
        }
    }

    private void loadData() {
        mLoadingAlertDialog = new LoadingAlertDialog(MyModeHtmlActivity.this);
        showLoadingView();
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myMode((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyMode>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showErrorView();
                showTest(mNetWorkError);
            }

            @Override
            public void onNext(MyMode myMode) {
                if ("1".equals(myMode.getState())) {
                    mMyMode = myMode;
                    loadData(myMode);
                } else {
                    showTest(mServerEror);
                }

            }
        });
        MyModeHtmlActivity.this.addSubscription(sbMyAccount);
    }

    private void loadData(MyMode myMode) {
        mLtObject.clear();
        mTopSize = myMode.getTopArray().size();
        mLtObject.add(new BaseMyMode());
        mLtObject.addAll(myMode.getTopArray());
        mLtObject.add(new BaseMyMode());
        mBotSize = myMode.getBotArray().size();
        mLtObject.addAll(myMode.getBotArray());
        mLtObject.add(new BaseMyMode());
        mLtObject.addAll(myMode.getAdvArray());
        mAdapter.notifyDataSetChanged();
    }


    private void delete(final BaseMyMode o) {
        mLoadingAlertDialog.show();
        int deleteId = 0;
        String deleteMode = null;
        if (o instanceof MyMode.TopArrayBean) {
            MyMode.TopArrayBean topArrayBean = (MyMode.TopArrayBean) o;
            deleteId = topArrayBean.getTopId();
            deleteMode = topArrayBean.getTable();
        } else if (o instanceof MyMode.BotArrayBean) {
            MyMode.BotArrayBean botArrayBean = (MyMode.BotArrayBean) o;
            deleteId = botArrayBean.getBotId();
            deleteMode = botArrayBean.getTable();
        } else if (o instanceof MyMode.AdvArrayBean) {
            MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;
            deleteId = advArrayBean.getAdvertid();
            deleteMode = advArrayBean.getTable();
        }
        Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleteAdv((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),deleteId, deleteMode)).subscribe(new Subscriber<BaseBean>() {
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
            public void onNext(BaseBean baseBean) {
                if (1 == baseBean.getState()) {//
                    showTest("删除成功");
                    if (o instanceof MyMode.TopArrayBean) {
                        mMyMode.getTopArray().remove(o);
                    } else if (o instanceof MyMode.BotArrayBean) {
                        mMyMode.getBotArray().remove(o);
                    } else if (o instanceof MyMode.AdvArrayBean) {
                        mMyMode.getAdvArray().remove(o);
                    }
                    loadData(mMyMode);
                } else {
                    showTest(mServerEror);
                }
            }
        });
        MyModeHtmlActivity.this.addSubscription(sbMyAccount);
    }


    private void putData() {
        MultiItemTypeSupport multiItemSupport = new MultiItemTypeSupport<BaseMyMode>() {
            @Override
            public int getLayoutId(int itemType) {
                //根据itemType返回item布局文件id
                switch (itemType) {
                    case ONE:
                        return R.layout.rvitem_mywork_mode_first;
                    case TWO:
                        return R.layout.rvitem_mywork_mode_two;
                }
                return R.layout.rvitem_mywork_mode_three;
            }

            @Override
            public int getItemViewType(int postion, BaseMyMode msg) {
                //根据当前的bean返回item type
                if (isTitle(postion)) {
                    Logger.i("Top position = " + postion + "   " + mBotSize);
                    return ONE;
                } else if (postion > 0 && postion <= mTopSize) {
                    Logger.i("YEMEI position = " + postion + "   " + mBotSize);
                    return TWO;
                } else if (postion > mTopSize && postion <= (mTopSize + 1 + mBotSize)) {
                    Logger.i("QR position = " + postion + "   " + mBotSize);
                    return TWO;
                }
                Logger.i("AD position = " + postion + "   " + mBotSize);
                return THREE;
            }
        };
        mAdapter = new MultiItemCommonAdapter<BaseMyMode>(MyModeHtmlActivity.this, mLtObject, multiItemSupport) {
            @Override
            public void convert(ViewHolder holder, final BaseMyMode o) {
                if (!isTitle(holder.getMyPosition())) {

                    holder.getView(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(o);
                        }
                    }); //这里的点击事件有问题
                    if (o instanceof MyMode.TopArrayBean) {
                        MyMode.TopArrayBean topArrayBean = (MyMode.TopArrayBean) o;
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + topArrayBean.getImg(), false);
                        holder.setText(R.id.tv, topArrayBean.getPublicNo());
                    } else if (o instanceof MyMode.BotArrayBean) {
                        MyMode.BotArrayBean botArrayBean = (MyMode.BotArrayBean) o;
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + botArrayBean.getImg(), false);
                        holder.setText(R.id.tv, botArrayBean.getTitle1());
                    } else if (o instanceof MyMode.AdvArrayBean) {
                        MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + advArrayBean.getAdimage(), false);
                        holder.setText(R.id.tv, advArrayBean.getAdtext());
                    }
                } else {
                    if (holder.getMyPosition() == 0) {
                        holder.setText(R.id.tv, "页眉");
                    } else if (holder.getMyPosition() == mTopSize + 1) {
                        holder.setText(R.id.tv, "页脚");
                    } else if (holder.getMyPosition() == mTopSize + 1 + mBotSize + 1) {
                        holder.setText(R.id.tv, "广告");
                    }
                }
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                if (!isTitle(position)) {
                    BaseMyMode baseMyMode = (BaseMyMode)o;
                    EventBus.getDefault().post(baseMyMode);
                    finish();
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(MyModeHtmlActivity.this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isTitle(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        mList.setLayoutManager(gridLayoutManager);
        mList.setAdapter(mAdapter);
    }

    @Subscribe
    public void onEventModeRefresh(MyModeRefreshEvent myModeRefreshEvent) {
        loadData();
    }


    @OnClick(R.id.ivAddMode)
    public void onClick() {
        baseStartActivity(MakeModeActivity.class);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
