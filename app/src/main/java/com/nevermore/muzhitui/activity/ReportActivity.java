package com.nevermore.muzhitui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.network.WorkService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import base.ImageUtil;
import base.SPUtils;
import base.view.LoadingAlertDialog;
import base.view.MyGridView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/3/8.
 */

public class ReportActivity extends BaseActivityTwoV {
    @BindView(R.id.tvReport1)
    TextView mTvReport1;
    @BindView(R.id.ivReport1)
    ImageView mIvReport1;
    @BindView(R.id.tvReport2)
    TextView mTvReport2;
    @BindView(R.id.ivReport2)
    ImageView mIvReport2;
    @BindView(R.id.tvReport3)
    TextView mTvReport3;
    @BindView(R.id.ivReport3)
    ImageView mIvReport3;
    @BindView(R.id.tvReport4)
    TextView mTvReport4;
    @BindView(R.id.ivReport4)
    ImageView mIvReport4;
    @BindView(R.id.tvReport5)
    TextView mTvReport5;
    @BindView(R.id.ivReport5)
    ImageView mIvReport5;
    @BindView(R.id.tvReport6)
    TextView mTvReport6;
    @BindView(R.id.ivReport6)
    ImageView mIvReport6;
    @BindView(R.id.gvReport)
    MyGridView mGvReport;
    List<ImageView> unVisables = new ArrayList<>();
    public static final int REQUEST_CLIP_IMAGE = 2028;
    private LoadingAlertDialog mLoadingAlertDialog;
    List<PhotoInfo> resultLists = new ArrayList<>();

    @BindView(R.id.svEditArticle)
    ScrollView mSvEditArticle;
    private MyImageViewAdapter adapter;
    private int report_id=1;
    private Uri uri;  //图片保存uri
    private File scaledFile;
    private GalleryFinal.OnHanlderResultCallback mOnhanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && !resultList.isEmpty()) {
                resultLists.clear();
                resultLists = resultList;
                scaledFile = ImageUtil.scal(resultList.get(0).getPhotoPath());


                    adapter = new MyImageViewAdapter();
                    mGvReport.setAdapter(adapter);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };


    @Override
    public void init() {
        setMyTitle("举报");
        showBack();
    final    String id=  getIntent().getStringExtra("id");
        showRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaledFile!=null){
                    uploadImg(scaledFile,Integer.parseInt(id),report_id);
                }else{
                    uploadImg(Integer.parseInt(id),report_id);
                }

            }
        });
        mLoadingAlertDialog = new LoadingAlertDialog(this);
        unVisables.add(mIvReport1);
        unVisables.add(mIvReport2);
        unVisables.add(mIvReport3);
        unVisables.add(mIvReport4);
        unVisables.add(mIvReport5);
        unVisables.add(mIvReport6);
        if (adapter == null) {
            adapter = new MyImageViewAdapter();
            mGvReport.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        mGvReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageUtil.getInstance().chooseImage("开始制作",mOnhanlderResultCallback, 1);
            }
        });

    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_report;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tvReport1, R.id.tvReport2, R.id.tvReport3, R.id.tvReport4, R.id.tvReport5, R.id.tvReport6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReport1:
                setVisable(mIvReport1, 1);
                report_id=1;
                break;
            case R.id.tvReport2:
                setVisable(mIvReport2, 2); report_id=2;
                break;
            case R.id.tvReport3:
                setVisable(mIvReport3, 3); report_id=3;
                break;
            case R.id.tvReport4:
                setVisable(mIvReport4, 4); report_id=4;
                break;
            case R.id.tvReport5:
                setVisable(mIvReport5, 5); report_id=5;
                break;
            case R.id.tvReport6:
                setVisable(mIvReport6, 6); report_id=6;

                break;

        }
    }

    private void setVisable(ImageView visable, int item) {

        for (int i = 0; i < unVisables.size(); i++) {
            if (visable == unVisables.get(i)) {
                unVisables.get(i).setVisibility(View.VISIBLE);

            } else {
                unVisables.get(i).setVisibility(View.GONE);
            }

        }
    }
    private Handler handler = new Handler();
    private void uploadImg(int target_id,int report_id) {



        mLoadingAlertDialog.show();

        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().reportUser((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),
                 target_id,report_id
               )
        ).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {
                    mLoadingAlertDialog.dismiss();

                    showTest("举报成功");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },300);
                } else {
                    showTest(code.getMsg());
                }

            }
        });

        addSubscription(sbMyAccount9);



    }
    private void uploadImg(File file,int target_id,int report_id) {



        mLoadingAlertDialog.show();

        Subscription sbMyAccount9 = wrapObserverWithHttp(WorkService.getWorkService().reportUser((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),
                ImageUtil.getInstance().wrapUploadImgRequest(file), target_id,report_id
                )
        ).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLoadingAlertDialog.dismiss();
                showTest("服务器连接失败");
            }

            @Override
            public void onNext(Code code) {
                if (code.getState().equals("1")) {
                    mLoadingAlertDialog.dismiss();

                    showTest("举报成功");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },300);
                } else {
                    showTest(code.getMsg());
                }


            }
        });

        addSubscription(sbMyAccount9);



    }
    private class MyImageViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return resultLists.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return resultLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = View.inflate(ReportActivity.this, R.layout.item_image, null);
                holder.image = (ImageView) view.findViewById(R.id.ivImage);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (position == resultLists.size()) {

                holder.image.setBackgroundResource(R.mipmap.ic_upload);


                if (position == 1) {
                    holder.image.setVisibility(View.GONE);
                    holder.image.setImageBitmap(null);
                }
            } else {

                if (resultLists.get(position).getPhotoPath() != null) {


                    holder.image.setImageBitmap(ImageUtil.getimage(resultLists.get(position).getPhotoPath()));
                }


            }

            return view;
        }
    }

    public class ViewHolder {

        public ImageView image;
    }
}
