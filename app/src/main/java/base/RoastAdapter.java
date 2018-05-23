package base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import base.network.RetrofitUtil;

/**
 * Created by Administrator on 2017/12/8.
 */

public class RoastAdapter extends PagerAdapter{

    private List<ImageView> images = new ArrayList<>();

    public RoastAdapter(List<String> imagesUrl, Context context){
        if(imagesUrl != null && !imagesUrl.isEmpty()){
            images.clear();
            for (int i = 0 ; i < imagesUrl.size() ; i++){
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage(RetrofitUtil.API_URL + RetrofitUtil.PROJECT_URL + imagesUrl.get(i),imageView);
                images.add(imageView);
            }
        }
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener){
        for (int i = 0 ; i < images.size() ; i++){
            final int j = i;
            final ImageView view = images.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null,view,j,0);
                }
            });
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(images.get(position));
        return images.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == object;
    }
}
