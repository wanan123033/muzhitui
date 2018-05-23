package com.nevermore.muzhitui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.activity.PhotoDetailActivity;
import com.nevermore.muzhitui.activity.editPhoto.ImageItem;
import com.nevermore.muzhitui.activity.editPhoto.OriginalArticleActivity;
import com.nevermore.muzhitui.module.bean.Bimp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import base.MyLogger;
import base.UIUtils;
import base.helper.PhotoActionHelper;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Administrator on 2017/10/23.
 */

public class ImageSecltorAdapter extends BaseAdapter {

    private final int addImageResouce;
    private List<String> paths;
    private Activity context;
    private int maxImgCount;
    private boolean crop = false;
    public static final int CLIP_IMAGEAll = 125;

    public ImageSecltorAdapter(Activity context, int maxImgCount, List<String> paths, int addImageResouce){
        this.maxImgCount = maxImgCount;
        this.paths = paths;
        this.context = context;
        this.addImageResouce = addImageResouce;
    }

    public void setCrop(boolean crop){
        this.crop = crop;
    }

    @Override
    public int getCount() {
        if(maxImgCount > paths.size()){
            return paths.size() + 1;
        }else if(maxImgCount == paths.size()){
            return maxImgCount;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(addImageResouce,null,false);
            holder = new ViewHolder();
            holder.iv_add_img = (ImageView) convertView.findViewById(R.id.iv_add_img);
            holder.iv_imgContent = (ImageView) convertView.findViewById(R.id.iv_imgContent);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        if(position < paths.size()) {
            holder.iv_imgContent.setVisibility(View.VISIBLE);
            holder.iv_add_img.setVisibility(View.GONE);
            MyLogger.kLog().e(paths.get(position));
            if(paths.get(position).startsWith("http")){
                ImageLoader.getInstance().displayImage(paths.get(position), holder.iv_imgContent);
            }else {
                ImageLoader.getInstance().displayImage("file://" + paths.get(position), holder.iv_imgContent);
            }
        }else {
            holder.iv_imgContent.setVisibility(View.GONE);
            holder.iv_add_img.setVisibility(View.VISIBLE);
        }
        holder.iv_imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //启动图片查看器删除图片
                Bimp.bmp.clear();
                for(String pic : paths){
                    ImageItem item = new ImageItem();
                    item.imageLoadPath = pic;
                    Bimp.bmp.add(item);
                }
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putExtra(PhotoDetailActivity.POSTION,position);
                intent.putExtra(PhotoDetailActivity.TYPE,1);
                intent.putExtra(PhotoDetailActivity.PHOTO_TYPE,"file://");
                context.startActivityForResult(intent,1025);
            }
        });
        holder.iv_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    GalleryFinal.openGalleryMuti(133, maxImgCount - paths.size(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (reqeustCode == 133 && resultList != null && !resultList.isEmpty()) {
                                for (int i = 0; i < resultList.size(); i++) {
                                    String path = resultList.get(i).getPhotoPath();
                                    String mOutputPath = new File(UIUtils.getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getPath();
                                    if (crop) {
                                        PhotoActionHelper.clipImage(context).input(path).output(mOutputPath).setPosition(position + i).setExtraHeight(UIUtils.dip2px(400)).maxOutputWidth(400)
                                                .requestCode(CLIP_IMAGEAll).start();
                                        continue;
                                    }

                                    paths.add(path);
                                }
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {

                        }
                    });

            }
        });
        return convertView;
    }

    public void removeData(String path){
        paths.remove(path);
        notifyDataSetChanged();
    }

    public void setImageList(List<String> imageList){
        paths.clear();
        paths.addAll(imageList);
        notifyDataSetChanged();
    }

    public List<String> getImageList() {
        return paths;
    }

    static class ViewHolder{
        ImageView iv_add_img;
        ImageView iv_imgContent;
    }
}
