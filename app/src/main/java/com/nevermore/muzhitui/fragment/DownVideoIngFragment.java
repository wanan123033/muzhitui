package com.nevermore.muzhitui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.VideoBean;
import com.nevermore.muzhitui.module.network.WorkService;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import base.BaseActivityTwoV;
import base.BaseFragment;
import base.MyLogger;
import base.recycler.ViewHolder;
import base.recycler.recyclerview.CommonAdapter;
import butterknife.BindView;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/1/29.
 */

public class DownVideoIngFragment extends BaseFragment{
    private List<VideoBean> videoBeans;

    @BindView(R.id.rv_downlist)
    RecyclerView rv_downlist;
    CommonAdapter adapter;
    private LinearLayoutManager llm;

    public void setDownVideos(List<VideoBean> videoBeans){
        this.videoBeans = videoBeans;
    }

    @Override
    public int createSuccessView() {
        return R.layout.fragment_downvideoing;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CommonAdapter<VideoBean>(getActivity(),R.layout.item_down_video,videoBeans) {
            @Override
            public void convert(ViewHolder holder, VideoBean o) {
                holder.setText(R.id.tv_videoName,o.videoTitle);
                ImageLoader.getInstance().displayImage(o.imageUrl, (ImageView) holder.getView(R.id.iv_img));
                ((ProgressBar)holder.getView(R.id.progressBar1)).setMax(o.max);
                ((ProgressBar)holder.getView(R.id.progressBar1)).setProgress(o.progress);
//                holder.setText(R.id.tv_videoSize,o.progress+"/"+o.max);
//                holder.setText(R.id.tv_video_percentage,(o.progress/o.max) + "");
//                downVideo(o,holder.getView(R.id.rel_root));
            }
        };
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_downlist.setLayoutManager(llm);
        rv_downlist.setAdapter(adapter);
        downVideo();
//        if (videoBeans == null || videoBeans.isEmpty()){
//            return;
//        }else {
//            adapter.replaceAllDate(videoBeans);
//        }

    }

    private void downVideo(final VideoBean bean, final View view) {
        ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().download(bean.videoUrl)).subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                bean.path = createFile();
                writeResponseBodyToDisk(responseBody, bean.path, new DownStatelistener() {
                    @Override
                    public void startDown(long max) {
                        MyLogger.kLog().e("max = " + max);
                        bean.max = (int) (max/1024/1024);
                        ProgressBar bar = view.findViewById(R.id.progressBar1);
                        TextView tvSize = view.findViewById(R.id.tv_videoSize);
                        tvSize.setText("0M/"+(max/1024/1024)+"M");
                        if(bar != null){
                            bar.setMax((int) max);
                        }
                    }

                    @Override
                    public void loadDown(long progress) {
                        //下载中
                        MyLogger.kLog().e("progress = " + progress);
                        bean.progress = (int) progress;
                        ProgressBar bar = view.findViewById(R.id.progressBar1);
                        TextView tvBage = view.findViewById(R.id.tv_video_percentage);
                        TextView tvSize = view.findViewById(R.id.tv_videoSize);
                        int pro = (int) (progress /1024/1024);
                        int bage = (int) (((double)pro / (double)bean.max) * 100.0);
                        tvSize.setText(pro + "M/"+bean.max + "M");
                        tvBage.setText(bage+"%");
                        if(bar != null){
                            bar.setProgress((int) progress);
                        }
                    }

                    @Override
                    public void endDown() {
                        videoBeans.remove(bean);
                        adapter.notifyDataSetChanged();
                        showTest("您的视频已经下载完成");
                        EventBus.getDefault().post(bean);

                    }
                });
            }
        }));
    }

    private void downVideo() {
        if (videoBeans == null || videoBeans.isEmpty()){
            return;
        }

        for(int i = 0 ; i < videoBeans.size() ; i++){
            final VideoBean bean = videoBeans.get(i);
            final int index = i;
            ((BaseActivityTwoV)getActivity()).addSubscription(wrapObserverWithHttp(WorkService.getWorkService().download(bean.videoUrl)).subscribe(new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    bean.path = createFile();
                    writeResponseBodyToDisk(responseBody, bean.path, new DownStatelistener() {
                        @Override
                        public void startDown(long max) {
                            MyLogger.kLog().e("max = " + max);
                            bean.max = (int) (max/1024/1024);
                            View childAt = rv_downlist.getChildAt(index);
                            ProgressBar bar = childAt.findViewById(R.id.progressBar1);
                            TextView tvSize = childAt.findViewById(R.id.tv_videoSize);
                            tvSize.setText("0M/"+(max/1024/1024)+"M");
                            if(bar != null){
                                bar.setMax((int) max);
                            }
                        }

                        @Override
                        public void loadDown(long progress) {
                            //下载中
                            MyLogger.kLog().e("progress = " + progress);
                            bean.progress = (int) progress;
                            View childAt = rv_downlist.getChildAt(index);
                            ProgressBar bar = childAt.findViewById(R.id.progressBar1);
                            TextView tvBage = childAt.findViewById(R.id.tv_video_percentage);
                            TextView tvSize = childAt.findViewById(R.id.tv_videoSize);
                            int pro = (int) (progress /1024/1024);
                            int bage = (int) (((double)pro / (double)bean.max) * 100.0);
                            tvSize.setText(pro + "M/"+bean.max + "M");
                            tvBage.setText(bage+"%");
                            if(bar != null){
                                bar.setProgress((int) progress);
                            }
                        }

                        @Override
                        public void endDown() {
                            videoBeans.remove(bean);
                            adapter.notifyDataSetChanged();
                            showTest("您的视频已经下载完成");
                            EventBus.getDefault().post(bean);

                        }
                    });
                }
            }));
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String path,DownStatelistener listener) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                listener.startDown(fileSize);
                int read = 0;
                while ((read = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    listener.loadDown(fileSizeDownloaded);
                }

                outputStream.flush();
                listener.endDown();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static String createFile(){
        // Create an image file name
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        String imageFileName = "video_" + System.currentTimeMillis();
        File image = null;
        try {
            image = new File(appDir,imageFileName+".mp4");
            if(!image.getParentFile().exists()){
                image.getParentFile().mkdirs();
            }
            if (!image.exists()){
                image.createNewFile();
            }

            MyLogger.kLog().e(image.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image.getAbsolutePath();
    }
    interface DownStatelistener{
        void startDown(long max);
        void loadDown(long progress);
        void endDown();
    }

}
