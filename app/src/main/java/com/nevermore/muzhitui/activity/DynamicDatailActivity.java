package com.nevermore.muzhitui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.module.BaseBean;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nevermore.muzhitui.module.bean.DynamicBean;
import com.nevermore.muzhitui.module.bean.DynamicInfo;
import com.nevermore.muzhitui.module.bean.ExceInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.MyLogger;
import base.SPUtils;
import base.UIUtils;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.util.EmojiFilter;
import base.util.ShareUtil;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/12.
 * 动态详情界面
 */

public class DynamicDatailActivity extends BaseActivityTwoV implements View.OnLayoutChangeListener{

    public static final String DISCUSS_TYPE = "discuss_type";
    @BindView(R.id.tv_dynamic_content)
    TextView tv_dynamic;
    @BindView(R.id.civ_topic)
    CircleImageView civ_topic;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.rv_photo_list)
    RecyclerView rv_photo_list;
    @BindView(R.id.tv_praise)
    TextView tv_praise;
    @BindView(R.id.rv_praise)
    RecyclerView rv_praise;
    @BindView(R.id.rv_discuss)
    RecyclerView rv_discuss;
    @BindView(R.id.ll_etText)
    LinearLayout ll_etText;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_reply)
    Button btn_reply;
    @BindView(R.id.btn_dicuss)
    Button btn_dicuss;
    @BindView(R.id.iv_praise)
    ImageView iv_praise;
    @BindView(R.id.tv_attention)
    TextView tv_attention;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.ll_menu)
    LinearLayout ll_menu;
    @BindView(R.id.tv_discuss)
    TextView tv_discuss;

    private InputMethodManager imm;

    public static final String DYNAMIC_ID = "dynamic_id";


    private LoadingAlertDialog mLoadingAlertDialog;
    private DynamicInfo dynamicInfo;

    private CommonAdapter<DynamicBean.Pic> picCommonAdapter;
    private CommonAdapter<DynamicInfo.Reply> replyCommonAdapter;
    private CommonAdapter<DynamicInfo.Pic> praiseCommonAdapter;
    private int position;
    int dynamicId;
    int discuss_type = 1;

    @Override
    public void init() {
        showBack();
        setMyTitle("动态详情");
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        dynamicId = getIntent().getIntExtra(DYNAMIC_ID,0);
        discuss_type = getIntent().getIntExtra(DISCUSS_TYPE,1);
        if(discuss_type == 0){
            ll_etText.setVisibility(View.VISIBLE);
            btn_dicuss.setVisibility(View.VISIBLE);
            btn_reply.setVisibility(View.GONE);
            ll_menu.setVisibility(View.GONE);
            showSoftInput(et_content);
        }else{
            ll_etText.setVisibility(View.GONE);
            btn_dicuss.setVisibility(View.GONE);
            btn_reply.setVisibility(View.GONE);
            ll_menu.setVisibility(View.VISIBLE);
            hideSoftInput();
        }

        //解决上下滑动卡顿的问题（主要原因：该RecycleView嵌套在ScrollView中事件冲突的问题）
        rv_discuss.setHasFixedSize(true);
        rv_discuss.setNestedScrollingEnabled(false);

        final List<DynamicBean.Pic> pics = new ArrayList<DynamicBean.Pic>();
        picCommonAdapter = new CommonAdapter<DynamicBean.Pic>(this,R.layout.item_img_content,pics) {
            @Override
            public void convert(ViewHolder holder, DynamicBean.Pic pic) {
                ImageLoader.getInstance().displayImage(pic.getPage_picpath(), (ImageView) holder.getView(R.id.img_photo));
                holder.getView(R.id.tv_seeAll).setVisibility(View.GONE);
                holder.getView(R.id.iv_playbg).setVisibility(View.GONE);
            }
        };


        picCommonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                Bimp.bmp.clear();
                for(DynamicBean.Pic pic : pics){
                    ImageItem item = new ImageItem();
                    item.imageLoadPath = pic.getPage_picpath();
                    Bimp.bmp.add(item);
                }
                Intent intent = new Intent(getApplicationContext(), PhotoDetailActivity.class);
                intent.putExtra(PhotoDetailActivity.POSTION,position);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });

        replyCommonAdapter = new CommonAdapter<DynamicInfo.Reply>(this,R.layout.item_reply_dynamic,new ArrayList<DynamicInfo.Reply>()) {
            @Override
            public void convert(final ViewHolder holder, final DynamicInfo.Reply reply) {

                ImageLoader.getInstance().displayImage(reply.reply_headimg, (ImageView) holder.getView(R.id.civ_topic));
                holder.setText(R.id.tv_name,reply.reply_user_name);
                if(reply.reply_type == 1){
                    holder.setText(R.id.tv_content,reply.reply_content);
                }else if(reply.reply_type == 2) {
                    SpannableStringBuilder string = new SpannableStringBuilder("回复 "+ reply.reply_user_name_p + ":" + reply.reply_content);
                    string.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent intent = new Intent(getApplicationContext(), DynamicPersonActivity.class);
                            intent.putExtra(DynamicPersonActivity.USERID, reply.reply_user_id_p + "");
                            startActivity(intent);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.parseColor("#576B95"));
                        }
                    }, 3, reply.reply_user_name_p.length() + 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    ((TextView)holder.getView(R.id.tv_content)).setText(string);
                    ((TextView)holder.getView(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());
                    holder.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replyDialog(dynamicInfo.oneDt.reply_infos.get(position).reply_loginid);
                        }
                    });
                }
                holder.setText(R.id.tv_date,reply.reply_time);
                if(reply.is_del_reply == 1){
                    holder.getView(R.id.tv_delete).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_reply).setVisibility(View.GONE);
                }else if(reply.is_del_reply == 2){
                    holder.getView(R.id.tv_reply).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_delete).setVisibility(View.GONE);
                }
                holder.setOnClickListener(R.id.tv_reply, new View.OnClickListener() {   //回复评论
                    @Override
                    public void onClick(View v) {
                        position = getPosition(holder);
                        //TODO 显示回复按钮
                        ll_menu.setVisibility(View.GONE);
                        replyDialog(dynamicInfo.oneDt.reply_infos.get(position).reply_loginid);
                    }
                });

                holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {   //删除评论或回复
                    @Override
                    public void onClick(View v) {
                        deleteReply(reply);
                    }
                });

                holder.setOnClickListener(R.id.civ_topic, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DynamicPersonActivity.class);
                        intent.putExtra(DynamicPersonActivity.USERID, reply.reply_loginid + "");
                        startActivity(intent);
                    }
                });


            }
        };
        replyCommonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                ll_menu.setVisibility(View.GONE);
                replyDialog(dynamicInfo.oneDt.reply_infos.get(position).reply_loginid);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position, ViewHolder viewHolder) {
                return false;
            }
        });
        praiseCommonAdapter = new CommonAdapter<DynamicInfo.Pic>(this,R.layout.item_praise,new ArrayList<DynamicInfo.Pic>()) {
            @Override
            public void convert(ViewHolder holder, final DynamicInfo.Pic pic) {
                ImageLoader.getInstance().displayImage(pic.headimg, (ImageView) holder.getView(R.id.civ_topic));
                holder.setOnClickListener(R.id.civ_topic, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),DynamicPersonActivity.class);
                        intent.putExtra(DynamicPersonActivity.USERID,pic.loginid+"");
                        startActivity(intent);
                    }
                });
            }
        };
        loadDynamic(dynamicId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        findViewById(R.id.ll_rootView).addOnLayoutChangeListener(this);
        Bimp.bmp.clear();
    }

    /**
     * 删除评论或者回复
     * @param reply
     */
    private void deleteReply(DynamicInfo.Reply reply) {
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().delReply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamicInfo.oneDt.id,reply.reply_id)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean o) {
                if("1".equals(o.state)){
                    showTest("删除成功");
                    loadDynamic(dynamicId);
                    EventBus.getDefault().post(DynamicPostedActivity.POSTED_STATE);
                }else{
                    showTest(o.msg);
                }
            }
        }));
    }

    private void replyDialog(int replyId) {
        ll_etText.setVisibility(View.VISIBLE);
        btn_reply.setVisibility(View.VISIBLE);
        btn_reply.setTag(replyId);
        btn_dicuss.setVisibility(View.GONE);
        et_content.requestFocus();
        imm.showSoftInput(et_content,0);
    }

    private void loadDynamic(int dynamicId) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().getOneDt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamicId)).subscribe(new Subscriber<DynamicInfo>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(DynamicInfo o) {
                if("1".equals(o.state)){
                    initDynamic(o);
                }else {
                    showTest(o.msg);
                }
            }
        }));
    }

    private void initDynamic(DynamicInfo dynamicInfo) {
        this.dynamicInfo = dynamicInfo;
        MyLogger.kLog().e(dynamicInfo.oneDt.content);
        tv_dynamic.setText(dynamicInfo.oneDt.content);
        tv_name.setText(dynamicInfo.oneDt.user_name);
        tv_date.setText(dynamicInfo.oneDt.time);
        tv_praise.setText(" "+dynamicInfo.oneDt.praise_count);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_photo_list.setLayoutManager(layoutManager);
        rv_photo_list.setAdapter(picCommonAdapter);
        picCommonAdapter.replaceAllDate(dynamicInfo.oneDt.pics);

        rv_discuss.setAdapter(replyCommonAdapter);
        replyCommonAdapter.replaceAllDate(dynamicInfo.oneDt.reply_infos);

        ImageLoader.getInstance().displayImage(dynamicInfo.oneDt.headimg,civ_topic);

        rv_praise.setAdapter(praiseCommonAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        rv_praise.setLayoutManager(staggeredGridLayoutManager);
        praiseCommonAdapter.replaceAllDate(dynamicInfo.oneDt.head_pics);

        if(dynamicInfo.oneDt.praise_type == 1){
            iv_praise.setBackgroundResource(R.mipmap.praise_finish);
        }else {
            iv_praise.setBackgroundResource(R.mipmap.praise_nomal);
        }

        //TODO 判断右上角的关注  删除  保存快照
        tv_delete.setVisibility(View.GONE);
        if(dynamicInfo.oneDt.loginid == Integer.parseInt((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""))){
            tv_attention.setVisibility(View.GONE);
        }else {
            tv_attention.setVisibility(View.VISIBLE);
            if(dynamicInfo.oneDt.fans_type == 0){
                tv_attention.setBackgroundResource(R.mipmap.attention);
            }else if (dynamicInfo.oneDt.fans_type == 1){
                tv_attention.setBackgroundResource(R.mipmap.attentioned);
            }
        }
        tv_discuss.setText("评论("+dynamicInfo.oneDt.reply_count+")");

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_dynamic_datail;
    }

    @OnClick({R.id.ll_snapshot,R.id.ll_wxzf,R.id.ll_discuss,R.id.btn_reply,R.id.btn_dicuss,R.id.iv_praise_list,R.id.iv_praise,R.id.tv_attention,R.id.ll_save_snap})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_discuss:  //显示输入框
                discussDialog();
                ll_menu.setVisibility(View.GONE);
                break;
            case R.id.ll_wxzf:   //转发
                wxzf();
                break;
            case R.id.ll_snapshot: //快照
                Intent intent = new Intent(this,SnapshotActivity.class);
                DynamicBean.Dynamic dynamic = info2Dynamic();
                intent.putExtra(SnapshotActivity.DYNAMIC_CONTENT,dynamic);
                startActivity(intent);
                break;
            case R.id.btn_dicuss:   //评论
                reply(dynamicInfo.oneDt.loginid,1);
                loadDynamic(dynamicId);
                break;
            case R.id.btn_reply:   //回复
                int replyId = (int) view.getTag();
                reply(replyId,2);
                ll_menu.setVisibility(View.VISIBLE);
                loadDynamic(dynamicId);
                break;
            case R.id.iv_praise:    //用户点赞
                praise(dynamicInfo.oneDt.praise_type == 1 ? 0 : 1);
                break;
            case R.id.iv_praise_list:  //点赞列表
                Intent intent1 = new Intent(this,PraisePersonListActivity.class);
                intent1.putExtra(PraisePersonListActivity.DYNAMIC_ID,dynamicInfo.oneDt.id);
                startActivity(intent1);
                break;
            case R.id.tv_attention:
                attention();
                break;
            case R.id.ll_save_snap:
                Intent intent2 = new Intent(DynamicDatailActivity.this,SnapshotActivity.class);
                intent2.putExtra(SnapshotActivity.RIGHT_STATE,1);
                intent2.putExtra(SnapshotActivity.DYNAMIC_CONTENT,info2Dynamic());
                startActivity(intent2);
                break;
        }
    }

    private void wxzf() {
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                final List<DynamicBean.Pic> pics = dynamicInfo.oneDt.pics;
                final List<File> files = new ArrayList<>();
                if(pics == null || pics.isEmpty()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showTest("没有图片不能一键转发");
                        }});
                    return;
                }
                for(int i = 0 ; i < pics.size() ; i++){
                    final String url = pics.get(i).getPage_picpath().replace("_s","");
                    File file = new File((String) SPUtils.get(url,""));
                    if(file.exists()){
                        files.add(file);
                        if(files.size() == pics.size()){
                            startActivity(ShareUtil.wxzf(dynamicInfo.oneDt.content,files));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingAlertDialog.dismiss();
                                }
                            });

                        }
                        continue;
                    }
                    addSubscription(wrapObserverWithHttp(WorkService.getWorkService().download(url)).subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                        }
                        @Override
                        public void onNext(ResponseBody body) {
                            File file = new File(saveFile(body));
                            files.add(file);
                            //标识该图片已经下载
                            SPUtils.put(url,file.getAbsolutePath());
                            if(files.size() == pics.size()){
                                startActivity(ShareUtil.wxzf(dynamicInfo.oneDt.content,files));
                                mLoadingAlertDialog.dismiss();
                            }
                        }
                    }));
                }

            }
        });
    }

    private void attention() {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().fans((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamicInfo.oneDt.loginid+"",dynamicInfo.oneDt.fans_type == 0 ? 1: 0)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if("1".equals(baseBean.state)) {
                    EventBus.getDefault().post(DynamicPostedActivity.POSTED_STATE);
                    if (dynamicInfo.oneDt.fans_type == 0) {
                        dynamicInfo.oneDt.fans_type = 1;
                        showTest("您已成功关注");
                    } else if (dynamicInfo.oneDt.fans_type == 1) {
                        showTest("您已取消关注");
                        dynamicInfo.oneDt.fans_type = 0;
                    }
                    if (dynamicInfo.oneDt.fans_type == 0) {
                        tv_attention.setBackgroundResource(R.mipmap.attention);
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIAN1));
                    } else if (dynamicInfo.oneDt.fans_type == 1) {
                        tv_attention.setBackgroundResource(R.mipmap.attentioned);
                        EventBus.getDefault().post(EventBusContanct.getInstance(EventBusContanct.TABMYFRAGMENT_REFRSH_FAN_JIA1));
                    }
                }else {
                    showTest(baseBean.msg);
                }
            }
        }));
    }

    @OnLongClick(R.id.tv_dynamic_content)
    public boolean onLongClick(View view){
        final PopupMenu menu = new PopupMenu(this,view);
        menu.inflate(R.menu.menu_copy);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_copy){
                    UIUtils.copy(dynamicInfo.oneDt.content);
                    showTest("复制成功");
                    menu.dismiss();
                }else if (item.getItemId() == R.id.action_report){
                    menu.dismiss();
                }else if (item.getItemId() == R.id.action_cancel){
                    menu.dismiss();
                }
                return true;
            }
        });
        menu.show();
        return true;
    }

    private void praise(final int type) {
        mLoadingAlertDialog.show();
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().praise((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),dynamicInfo.oneDt.id,type,dynamicInfo.oneDt.loginid)).subscribe(new Subscriber<ExceInfo>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeErrorView();
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(ExceInfo baseBean) {
                if(baseBean.getState() == 1){
                    EventBus.getDefault().post(DynamicPostedActivity.POSTED_STATE);
                    if(type == 1){
                        dynamicInfo.oneDt.praise_type = 1;
                        showTest("您已成功点赞！");
                    }else{
                        dynamicInfo.oneDt.praise_type = 0;
                        showTest("您已取消点赞！");
                    }
                    if(dynamicInfo.oneDt.praise_type == 1){
                        iv_praise.setBackgroundResource(R.mipmap.praise_finish);
                    }else {
                        iv_praise.setBackgroundResource(R.mipmap.praise_nomal);
                    }
                    loadDynamic(dynamicId);
                }else{
                    showTest(baseBean.getMsg());
                }
            }
        }));
    }

    private void reply(int reply_parent_id,int type) {
        String content = et_content.getText().toString();
        discussDynamic(content,dynamicInfo.oneDt.id,type,reply_parent_id);
    }

    private void discussDialog() {
        ll_etText.setVisibility(View.VISIBLE);
        btn_reply.setVisibility(View.GONE);
        btn_dicuss.setVisibility(View.VISIBLE);
        et_content.requestFocus();
        imm.showSoftInput(et_content,0);
    }


    /**
     * 评论动态或者回复别人的评论
     * @param content  内容
     * @param pagedid  动态ID
     * @param type  1：评论 2：回复
     * @param reply_parent_id ：被 评论或回复 的人 的ID
     */
    private void discussDynamic(String content,int pagedid,int type,int reply_parent_id) {
        mLoadingAlertDialog.show();
        content = EmojiFilter.filterEmoji(content);
        addSubscription(wrapObserverWithHttp(WorkService.getWorkService().reply((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),pagedid,content,type,reply_parent_id)).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                removeLoadingView();
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onNext(BaseBean o) {
                if ("1".equals(o.state)){
                    EventBus.getDefault().post(DynamicPostedActivity.POSTED_STATE);
                    showTest(o.msg);
                    et_content.setText("");
                    loadDynamic(dynamicId);
                    hideSoftInput();
                }else{
                    showTest(o.msg);
                }
            }
        }));
    }

    private DynamicBean.Dynamic info2Dynamic() {
        DynamicBean.Dynamic dynamic = new DynamicBean.Dynamic();
        dynamic.setContent(dynamicInfo.oneDt.content);
        dynamic.setPics(dynamicInfo.oneDt.pics);
        dynamic.setFans_type(dynamicInfo.oneDt.fans_type);
        dynamic.setPraise_count(dynamicInfo.oneDt.praise_count);
        dynamic.setHeadimg(dynamicInfo.oneDt.headimg);
        dynamic.setUser_name(dynamicInfo.oneDt.user_name);
        dynamic.setId(dynamicInfo.oneDt.id);
        return dynamic;
    }
    private int screenHeight,keyHeight;
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            ll_etText.setVisibility(View.GONE);
            ll_menu.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideSoftInput() {
        super.hideSoftInput();
        ll_menu.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSoftInput(EditText editText) {
        super.showSoftInput(editText);
        ll_menu.setVisibility(View.GONE);
    }
    public String saveFile(ResponseBody body) {
        byte[] bufd = null;
        try {
            bufd = body.bytes();
            return ImageUtil.saveImageToGallery(BitmapFactory.decodeByteArray(bufd,0,bufd.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
