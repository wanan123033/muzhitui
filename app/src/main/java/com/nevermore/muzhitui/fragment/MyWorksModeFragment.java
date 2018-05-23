package com.nevermore.muzhitui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.muzhitui.AdMakeActivity;
import com.nevermore.muzhitui.MakeModeActivity;
import com.nevermore.muzhitui.QRMakeActivity;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.StartMakeActivity;
import com.nevermore.muzhitui.event.MyModeRefreshEvent;
import com.nevermore.muzhitui.event.ObjectEvent;
import com.nevermore.muzhitui.event.PublishedModelEvent;
import com.nevermore.muzhitui.module.bean.BaseBean;
import com.nevermore.muzhitui.module.bean.BaseMyMode;
import com.nevermore.muzhitui.module.bean.MyMode;
import com.nevermore.muzhitui.module.network.WorkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.SPUtils;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.MultiItemCommonAdapter;
import base.recycler.recyclerview.MultiItemTypeSupport;
import base.recycler.recyclerview.OnItemClickListener;
import base.util.CacheUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * 我的模板Fragment
 */
public class MyWorksModeFragment extends BaseFragment {


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
    public int published = 0, itemposition = 0;

    @Override
    public int createSuccessView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return R.layout.fragment_mywork_mode;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        showLoadingView();
        final Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().myMode((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))).subscribe(new Subscriber<MyMode>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                removeErrorView();
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
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
    }

    private void loadData(MyMode myMode) {
        mLtObject.clear();
        if (isAdd==false){//当isAdd为false的时候就将页面，页脚显示在页面，


        mTopSize = myMode.getTopArray().size();
        if (mTopSize != 0) {
            BaseMyMode baseMyMode = new BaseMyMode();
            baseMyMode.setModeTable("页眉");
            mLtObject.add(baseMyMode);
            mLtObject.addAll(myMode.getTopArray());
        } else {
            mTopSize = -1;
        }

        mBotSize = myMode.getBotArray().size();
        if (mBotSize != 0) {
            BaseMyMode baseMyMode = new BaseMyMode();
            baseMyMode.setModeTable("页脚");
            mLtObject.add(baseMyMode);
            mLtObject.addAll(myMode.getBotArray());
        } else {
            mBotSize = -1;
        }
        }
        if (myMode.getAdvArray().size() != 0) {
            BaseMyMode baseMyMode = new BaseMyMode();
            baseMyMode.setModeTable("广告");
            mLtObject.add(baseMyMode);
            mLtObject.addAll(myMode.getAdvArray());
        }
        mAdapter.notifyDataSetChanged();
    }


    private void delete(final BaseMyMode o) {
        mLoadingAlertDialog.show();//为什么set方法没执行 gson不是调用的set方法
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
        final Subscription sbMyAccount = wrapObserverWithHttp(WorkService.getWorkService().deleteAdv((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),deleteId, deleteMode)).subscribe(new Subscriber<BaseBean>() {
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
        ((BaseActivityTwoV) getActivity()).addSubscription(sbMyAccount);
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
                    return ONE;
                } else if (postion > 0 && postion <= mTopSize) {
                    return TWO;
                } else if (postion > mTopSize && postion <= (mTopSize + 1 + mBotSize)) {
                    return TWO;
                }
                return THREE;
            }
        };
        mAdapter = new MultiItemCommonAdapter<BaseMyMode>(getActivity(), mLtObject, multiItemSupport) {
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
                        Log.e("页眉头像：",topArrayBean.getImg());
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + topArrayBean.getImg(), false);
                        holder.setText(R.id.tv, topArrayBean.getPublicNo());
                    } else if (o instanceof MyMode.BotArrayBean) {
                        MyMode.BotArrayBean botArrayBean = (MyMode.BotArrayBean) o;
                        Log.e("页脚头像：",botArrayBean.getImg());
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + botArrayBean.getImg(), false);
                        holder.setText(R.id.tv, botArrayBean.getTitle1());
                    } else if (o instanceof MyMode.AdvArrayBean) {
                        MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;
                        Log.e("广告头像：",advArrayBean.getAdimage());
                        holder.setImageURL(R.id.iv, RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + advArrayBean.getAdimage(), false);
                        holder.setText(R.id.tv, advArrayBean.getAdtext());
                    }
                } else {
                    //   if (holder.getMyPosition() == 0) {
                    holder.setText(R.id.tv, o.getModeTable());
               /*     } else if (holder.getMyPosition() == mTopSize + 1) {
                        holder.setText(R.id.tv, "页脚");
                    } else if (holder.getMyPosition() == mTopSize + 1 + mBotSize + 1) {
                        holder.setText(R.id.tv, "广告");
                    }*/
                }
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                if (!isTitle(position)) {
                    if (published == 0) {//当published=0 时 则为原始模板点击处理
                        if (o instanceof MyMode.TopArrayBean) {
                            MyMode.TopArrayBean topArrayBean = (MyMode.TopArrayBean) o;
                            Intent intent = new Intent(getActivity(), StartMakeActivity.class);
                            intent.putExtra(YEMEI, topArrayBean);
                            getActivity().startActivity(intent);

                        } else if (o instanceof MyMode.BotArrayBean) {
                            MyMode.BotArrayBean botArrayBean = (MyMode.BotArrayBean) o;
                            Intent intent = new Intent(getActivity(), QRMakeActivity.class);
                            intent.putExtra(YEJIAO, botArrayBean);
                            getActivity().startActivity(intent);
                        } else if (o instanceof MyMode.AdvArrayBean) {
                            MyMode.AdvArrayBean advArrayBean = (MyMode.AdvArrayBean) o;
                            Intent intent = new Intent(getActivity(), AdMakeActivity.class);
                            intent.putExtra(AD, advArrayBean);
                            getActivity().startActivity(intent);
                        }
                    } else {//当published=1 时 则为从原创选择模板处进入处理
                        if (isAdd){
                            if (o instanceof MyMode.AdvArrayBean) {
                                EventBus.getDefault().post(new ObjectEvent(2,o));//添加视频模板
                                getActivity().finish();
                            }else{
                                showTest("添加广告只能添加视频模板");
                            }
                            Log.e("add =type=","点击添加模板if ");
                        }else{
                            Log.e("add =type=","点击添加模板else ");
                            EventBus.getDefault().post(new ObjectEvent(1,itemposition,o));//添加模板 页面 页脚 ，视频
                            getActivity().finish();

                        }
                    }

                }
            }
            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
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

    private boolean isAdd=false;
    @Subscribe
    public void onEventMainThread(PublishedModelEvent mPublishedModelEvent) {
        Log.e("add =type=","点击添加模板11"+mPublishedModelEvent.getState());
        published = 1;
        if (mPublishedModelEvent.getState()==1){
            isAdd=false;
            itemposition=mPublishedModelEvent.getPosition();
        }else if (mPublishedModelEvent.getState()==2){
            isAdd=true;
        }

    }

    @OnClick(R.id.ivAddMode)
    public void onClick() {    //我的模板的添加按钮

        if (isAdd){//isAdd为真时，代表添加广告模板，直接跳转到广告模板制作业面
            baseStartActivity(AdMakeActivity.class);
        }else{//isAdd为false时，跳转到所有模板制作业面
            baseStartActivity(MakeModeActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
