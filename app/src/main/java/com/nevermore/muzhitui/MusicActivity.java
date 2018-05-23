package com.nevermore.muzhitui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.nevermore.muzhitui.event.EditInfoEvent;
import com.nevermore.muzhitui.module.bean.Music;
import com.nevermore.muzhitui.module.bean.MusicDataBean;
import com.nevermore.muzhitui.module.bean.SongInfo;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.network.RetrofitUtil;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.recycler.recyclerview.OnItemClickListener;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hehe on 2016/6/15.
 */
public class MusicActivity extends BaseActivityTwoV{
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.etkeyword)
    EditText et_MusicKeyword;
    @BindView(R.id.ivb_search)
    TextView btn_search;
    private String mCurrentSrc;
    private CommonAdapter mAdapter;
    private LoadingAlertDialog mLoadingAlertDialog;
//    List<Music.MusicListBean> mLtObject = new ArrayList<>();

    List<SongInfo> songinfos = new ArrayList<>();
    public static final String KEY_SRC = "SRC";
    private int max = 0;
    private int min = 0;

    List<String> hashs = new ArrayList<>();

    private MediaPlayer mPlayer;
    SongInfo musicListBean;

    @BindView(R.id.toolbar1)
    Toolbar mToolbar;
    @BindView(R.id.iv_back)
    ImageButton iv_back;
    @BindView(R.id.pcFlyt)
    PtrClassicFrameLayout mPcFlyt;
    String intentName;
    CircleImageView img;
    @Override
    public void init() {
        showBackBtn();
        setMyTitle("添加音乐");
        hideActionBar();
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        mCurrentSrc = getIntent().getStringExtra(KEY_SRC);
        intentName = getIntent().getStringExtra("intentName");

        mAdapter = new CommonAdapter<SongInfo>(this, R.layout.rvitem_music, songinfos) {
            @Override
            public void convert(ViewHolder holder, SongInfo o) {
//                holder.setText(R.id.tvName, o.getName());
//                Logger.i(o.getSrc() +"    " +mCurrentSrc );
                if (o.getUrl().equals(mCurrentSrc)) {
                    holder.setVisible(R.id.ivChoice, true);
                } else {
                    holder.setVisible(R.id.ivChoice, false);
                }
                holder.setText(R.id.tv_gequName,o.getSongName()); //歌曲名称
                holder.setText(R.id.tv_geshou,o.getSingerName()); //歌手
                img = (CircleImageView) holder.getView(R.id.iv_gequIcon);
                if(!TextUtils.isEmpty(o.getImgUrl())) {
                    img.setBorderWidth(0);
                    ImageLoader.getInstance().displayImage(o.getImgUrl().replace("{size}", "100"), img); //网络获取
                }
                if(TextUtils.isEmpty(o.getSingerName())){
                    holder.setText(R.id.tv_geshou,"本地");
                }

            }
        };

        loadData();
        et_MusicKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_search.setText("搜索");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //设置下拉刷新  上拉加载
        final RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                btn_search.setText("完成");
                musicListBean = songinfos.get(position);
                //TODO 播放音乐 并改搜索为完成按钮
                Log.i("TAG",musicListBean.getUrl());
                setPlayer(musicListBean.getUrl());
                mCurrentSrc = musicListBean.getUrl();
                recyclerAdapterWithHF.notifyDataSetChanged();
            }
        });
        mList.setAdapter(recyclerAdapterWithHF);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        mPcFlyt.addPtrUIHandler(header);
        mPcFlyt.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                songinfos.clear();
                min = 0;
                mCurrenPager=1;
                loadDataPage( mCurrenPager);
            }

        });


        mPcFlyt.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                min = 0;
                loadDataPage(++mCurrenPager);
            }
        });
    }

    private void showBackBtn() {
        iv_back.setImageResource(R.drawable.ic_back);
        iv_back.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
                onBackPressed();
            }
        });

    }


//    private void loadData() {
//        mLoadingAlertDialog.show();
//        Subscription sbMusicList = wrapObserverWithHttp(WorkService.getWorkService().musicList()).subscribe(new Subscriber<Music>() {
//            @Override
//            public void onCompleted() {
//                mLoadingAlertDialog.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mLoadingAlertDialog.dismiss();
//                showErrorView();
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(Music music) {
//                if ("1".equals(music.getState())) {
////                    mAdapter.addDate(music.getMusicList());
//                    List<Music.MusicListBean> musicList = music.getMusicList();
//                    List<SongInfo> infos = new ArrayList<SongInfo>();
//                    for (Music.MusicListBean musicListBean : musicList) {
//                        SongInfo info = new SongInfo();
//                        info.setSongName(musicListBean.getName());
//                        info.setUrl(musicListBean.getSrc());
//                        infos.add(info);
//                    }
//                    mAdapter.addDate(infos);
//                    if(img != null)
//                        img.setBorderWidth(1);
//                } else {
//                    showTest("网络异常");
//                }
//
//            }
//        });
//        addSubscription(sbMusicList);
//    }
    private void loadData() {
        mLoadingAlertDialog.show();
        Subscription sbMusicList = wrapObserverWithHttp(WorkService.getWorkService().getYCMusics()).subscribe(new Subscriber<MusicDataBean>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingAlertDialog.dismiss();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(MusicDataBean music) {
                if ("1".equals(music.getState())) {
                    List<MusicDataBean.MusicArrayBean> musicList = music.getMusicArray();
                    List<SongInfo> infos = new ArrayList<SongInfo>();
                    for(MusicDataBean.MusicArrayBean musicArrayBean : musicList){
                        for (MusicDataBean.ListBean bean : musicArrayBean.getList()){
                            SongInfo info = new SongInfo();
                            info.setSongName(bean.getName());
                            info.setUrl(bean.getUrl());
                            infos.add(info);
                        }
                    }
                    mAdapter.addDate(infos);
                    if(img != null)
                        img.setBorderWidth(1);
                }else {
                    showTest("网络异常");
                }
            }
        });
        addSubscription(sbMusicList);
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_music;
    }

    private int mCurrenPager;
    private String keyword;
    @OnClick(R.id.ivb_search)
    public void searchMusic(View view){
        String text = btn_search.getText().toString().trim();
        if(text.equals("搜索")) {
            String keyword = et_MusicKeyword.getText().toString().trim();
            this.keyword = keyword;
            if(TextUtils.isEmpty(keyword))
                return;
            songinfos.clear();
            min = 0;
            loadDataPage(1);
        }else if(text.equals("完成")){
            if(!TextUtils.isEmpty(intentName) && intentName.equals("OriginalArticleActivity")) {
                EventBus.getDefault().post(new EditInfoEvent(musicListBean, 102));
            }else{
                EventBus.getDefault().post(musicListBean);
            }
            finish();
        }
    }
    //设置音乐 并播放
    private void setPlayer(String url) {
        if(!url.startsWith("http")){
            url = RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + url;
        }
        if (mPlayer == null){
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            showTest("You might not set the URI correctly!111");
        } catch (SecurityException e) {
            showTest("You might not set the URI correctly!222");
        } catch (IllegalStateException e) {
            showTest("You might not set the URI correctly333!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            showTest("You might not set the URI correctly444!");
        } catch (IOException e) {
            showTest("You might not set the URI correctly555!");
        }
        mPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
    private void loadDataPage(final int page){
        final List<SongInfo> songinfoList = new ArrayList<>();
        mLoadingAlertDialog.show();
        //获取音乐的搜索结果
        //http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=%E5%B0%8F%E5%B9%B8%E8%BF%90&page=1&pagesize=20&showtype=1
        RequestParams params = new RequestParams("http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=" + keyword + "&page="+page+"&pagesize=20&showtype=1");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e("TAG", s);
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray info = json.getJSONObject("data").getJSONArray("info");
                    hashs.clear();
                    max = info.length();
                    songinfoList.clear();
                    if(info.length() == 0 && page > 1){
                        showTest("已经是最后一页了！");
                        mLoadingAlertDialog.dismiss();
                        removeLoadingView();
                        mPcFlyt.setLoadMoreEnable(true);
                        mPcFlyt.loadMoreComplete(true);
                        mPcFlyt.refreshComplete();
                        return;
                    }else if(info.length() == 0 && page == 1){
                        showTest("没搜到您想要的音乐");
                        mLoadingAlertDialog.dismiss();
                        removeLoadingView();
                        mPcFlyt.setLoadMoreEnable(true);
                        mPcFlyt.loadMoreComplete(true);
                        mPcFlyt.refreshComplete();
                        return;
                    }
                    for (int i = 0; i < max; i++) {
                        hashs.add(info.getJSONObject(i).getString("hash"));
                        RequestParams para = new RequestParams("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + info.getJSONObject(i).getString("hash"));
                        x.http().get(para, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String s) {
                                try {
                                    SongInfo info = new SongInfo();
                                    JSONObject json = new JSONObject(s);
                                    info.setFileSize(json.getInt("fileSize"));
                                    info.setExtName(json.getString("extName"));
                                    String imgUrl = json.getString("imgUrl").replace("{size}", "100");
                                    info.setImgUrl(imgUrl);
                                    info.setSingerName(json.getString("singerName"));
                                    info.setSongName(json.getString("songName"));
                                    info.setUrl(json.getString("url"));
                                    songinfos.add(info);
                                    songinfoList.add(info);
                                    Log.e("TAG", info.toString());
                                    min++;
                                    Log.e("TAG", "min=" + min + ",max=" + max);
                                    if (min == max) {
                                        mAdapter.addDate(songinfoList);
                                        mLoadingAlertDialog.dismiss();
                                        removeLoadingView();
                                        mPcFlyt.setLoadMoreEnable(true);
                                        mPcFlyt.loadMoreComplete(true);
                                        mPcFlyt.refreshComplete();
                                        Log.i("TAG","songinfos.size="+songinfos.size()+"----page="+page);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable, boolean b) {

                            }

                            @Override
                            public void onCancelled(CancelledException e) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                    Log.e("TAG", hashs.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
