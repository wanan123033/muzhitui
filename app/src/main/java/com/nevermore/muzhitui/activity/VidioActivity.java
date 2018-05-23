package com.nevermore.muzhitui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;

import com.nevermore.muzhitui.adapter.GvVidelAdapter;
import com.nevermore.muzhitui.adapter.LvQuickReplyAdapter;

import com.nevermore.muzhitui.module.bean.Video;
import com.nevermore.muzhitui.module.json.QuickReplyBean;
import com.nevermore.muzhitui.module.json.QuickReplyJson;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.UIUtils;
import base.network.RetrofitUtil;
import base.view.LoadingAlertDialog;
import base.view.MyExpandaleListView;
import base.view.MyGridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by simone on 2016/12/14.
 */

public class VidioActivity extends BaseActivityTwoV {

    @BindView(R.id.gvVidio)
    MyGridView mGvVidio;

    @BindView(R.id.svEditArticle)
    ScrollView mSvEditArticle;
    @BindView(R.id.ivVedio)
    ImageView mIvVedio;
    @BindView(R.id.tvVideoText)
    TextView mTvVideoText;

    @BindView(R.id.gvVidioText)
    MyExpandaleListView gvVidioText;
    private LoadingAlertDialog mLoadingAlertDialog;
    private GvVidelAdapter adpter;

  /*  String path;
    boolean isShared;*/

    List<QuickReplyBean> mQuickReplyBean;

    @Override
    public void init() {
        showBack();
        setMyTitle("视频");
        mLoadingAlertDialog = new LoadingAlertDialog(VidioActivity.this);

        getVideoCourse(1);
        getVideoCourse(2);

        mGvVidio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Video.VedioArrayBean mVedioArray = (Video.VedioArrayBean) adpter.getItem(position);
                boolean isShared;
                if (mVedioArray.getIs_share() == 0) {
                    isShared = true;
                } else {
                    isShared = false;
                }
                Log.e("share list:", mVedioArray.getIs_share() + "\t是否分享：" + isShared + "\t需要复制的链接：" + mVedioArray.getJump_url_c());
                Log.e("share item:", mVedioArray.toString());
                Intent intent = new Intent(VidioActivity.this, PageLookActivity.class);
                intent.putExtra(PageLookActivity.KEY_URL, mVedioArray.getJump_url());
                intent.putExtra(PageLookActivity.KEY_IS_SHARE, isShared);//0能分享链接  1 不能分享链接
                intent.putExtra(PageLookActivity.KEY_IS_SHARE_LINK, mVedioArray.getJump_url_c());//不能分享时需复制链接
                intent.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                startActivity(intent);
            }
        });


    }

    private void setText() {
        mQuickReplyBean = QuickReplyJson.getSampleMusic();

        final LvQuickReplyAdapter adapter = new LvQuickReplyAdapter(mQuickReplyBean, this, gvVidioText, true);
        gvVidioText.setAdapter(adapter);
        adapter.notifyDataSetChanged();//设置二级列表默认展开
        gvVidioText.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                if (mQuickReplyBean.get(groupPosition).getList().get(childPosition).isCheck()) {
                    mQuickReplyBean.get(groupPosition).getList().get(childPosition).setCheck(false);
                    adapter.notifyDataSetChanged();
                } else {
                    setAdatper(mQuickReplyBean, groupPosition, childPosition, adapter);
                }


                return false;
            }
        });
    }

    /**
     * @param mQuickReplyBean 音乐数据集合
     * @param groupPosition   父类position
     * @param childPosition   子类
     * @param adapter         适配器
     */

    private void setAdatper(List<QuickReplyBean> mQuickReplyBean, int groupPosition, int childPosition, LvQuickReplyAdapter adapter) {

        for (int i = 0; i < mQuickReplyBean.size(); i++) {

            for (int j = 0; j < mQuickReplyBean.get(i).getList().size(); j++) {


                mQuickReplyBean.get(i).getList().get(j).setCheck(false);

            }

        }

        mQuickReplyBean.get(groupPosition).getList().get(childPosition).setCheck(true);


        adapter.notifyDataSetChanged();
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_vidio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void getVideoCourse(final int flag) {
        mLoadingAlertDialog.show();
        Subscription sbGetVideoCourse = wrapObserverWithHttp(WorkService.getWorkService().getVideoCourse((String) SPUtils.get(SPUtils.GET_LOGIN_ID, ""), flag)).subscribe(new Subscriber<Video>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
                e.printStackTrace();
            }

            @Override
            public void onNext(final Video video) {
                if (video.getState().equals("1")) {
                    if (flag == 1) {
                        ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + video.getVedioArray().get(0).getImg_url(),
                                mIvVedio, ImageUtil.getInstance().getBaseDisplayOption());
                        mTvVideoText.setText(video.getVedioArray().get(0).getTitle());

                        int share = video.getVedioArray().get(0).getIs_share();
                        final boolean isShared;
            //0能分享链接  1 不能分享链接
                        if (share == 0) {
                            isShared = true;
                        } else {
                            isShared = false;
                        }
                        Log.e("share:", share + "\t" + isShared + "\t加载链接：" + video.getVedioArray().get(0).getJump_url() + "\t需要复制的链接：" + video.getVedioArray().get(0).getJump_url_c());
                        mIvVedio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(VidioActivity.this, PageLookActivity.class);
                                intent.putExtra(PageLookActivity.KEY_URL, video.getVedioArray().get(0).getJump_url());
                                intent.putExtra(PageLookActivity.KEY_IS_SHARE, isShared);////0能分享链接  1 不能分享链接
                                intent.putExtra(PageLookActivity.KEY_IS_SHARE_LINK, video.getVedioArray().get(0).getJump_url_c());//不能分享时需复制链接
                                intent.putExtra(PageLookActivity.KEY_TEXT, "这个软件可以把你的产品或项目推广到整个社交生态圈的推广神器！");

                                startActivity(intent);
                            }
                        });
                    } else {
                        mLoadingAlertDialog.dismiss();
                        if (adpter == null) {
                            adpter = new GvVidelAdapter(VidioActivity.this, video.getVedioArray());
                            mGvVidio.setAdapter(adpter);
                        } else {
                            adpter.notifyDataSetChanged();
                        }
                        setText();
                    }


                } else {
                    showTest("视频加载失败");
                }


            }
        });
        addSubscription(sbGetVideoCourse);

    }


}
