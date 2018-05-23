package com.nevermore.muzhitui.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nevermore.muzhitui.EventBusContanct;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.VideoFilter;
import com.nevermore.muzhitui.module.bean.VideoBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.smtt.sdk.TbsVideo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import base.BaseFragment;
import base.ImageUtil;
import base.MyLogger;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import base.thread.BaseRunnable;
import base.thread.ThreadManager;
import base.view.LoadingAlertDialog;
import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/29.
 */

public class DownVideoedFragment extends BaseFragment {

    @BindView(R.id.rv_downlist)
    RecyclerView rv_downlist;
    CommonAdapter adapter;

    private LoadingAlertDialog mLoadingAlertDialog;

    @Override
    public int createSuccessView() {
        return R.layout.fragment_downvideoing;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingAlertDialog = new LoadingAlertDialog(getActivity());
        adapter = new CommonAdapter<VideoBean>(getActivity(),R.layout.item_down_video,new ArrayList<VideoBean>()) {
            @Override
            public void convert(ViewHolder holder, final VideoBean o) {
                holder.setText(R.id.tv_videoName,o.videoTitle);
                try {
                    ImageLoader.getInstance().displayImage(Uri.fromFile(new File(o.videoImgPath)).toString(),(ImageView) holder.getView(R.id.iv_img));
                }catch (Exception e){
                    e.printStackTrace();
                }
                holder.getView(R.id.progressBar1).setVisibility(View.INVISIBLE);
                SimpleDateFormat data = new SimpleDateFormat("HH:mm:ss");
                holder.setText(R.id.tv_videoSize,data.format(o.duration).replace("08","00"));
                holder.setText(R.id.tv_video_percentage,o.max + "M");
                holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {   //删除视频,并删除文件
                    @Override
                    public void onClick(View view) {
                        File file = new File(o.path);
                        file.delete();
                        showTest("删除完成");
                        adapter.removeDate(o);
                        EventBus.getDefault().post(o);
                    }
                });

                holder.setOnClickListener(R.id.iv_img, new View.OnClickListener() {   //播放视频
                    @Override
                    public void onClick(View view) {
                        TbsVideo.openVideo(getActivity(),o.path);
                    }
                });
            }
        };
        rv_downlist.setAdapter(adapter);
        getVideoList();
    }

    public void getVideoList(){
        mLoadingAlertDialog.show();
        ThreadManager.getInstance().run(new BaseRunnable() {
            @Override
            public void run() {
                File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
                final List<VideoBean> beans = new ArrayList<>();
                String[] files = appDir.list(new VideoFilter());
                for (int i = 0 ; i < files.length ; i++){
                    VideoBean videoBean = new VideoBean();
                    videoBean.path = appDir.getAbsolutePath() + File.separator + files[i];
                    videoBean.videoImgPath = getVideoThumbnail(videoBean.path);
                    videoBean.videoTitle = new File(videoBean.path).getName();
                    videoBean.duration = getDuration(videoBean.path);
                    videoBean.max = (int) (new File(videoBean.path).length() /1024 /1024);
                    beans.add(videoBean);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingAlertDialog.dismiss();
                        adapter.replaceAllDate(beans);
                    }
                });
            }

            @Override
            public void run(Object... objs) {

            }
        });



    }
    public String getVideoThumbnail(String filePath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(filePath);// videoPath 本地视频的路径
        Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        try {
            File imageFile = ImageUtil.createImageFile();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(imageFile));
            bitmap.recycle();
            bitmap = null;
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getDuration(String path){
        MediaPlayer meidaPlayer = new MediaPlayer();
        try {
            meidaPlayer.setDataSource(path);
            meidaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        long time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
        meidaPlayer.stop();
        meidaPlayer = null;
        return time;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEMMessage(VideoBean bean) {
        getVideoList();
    }
}
