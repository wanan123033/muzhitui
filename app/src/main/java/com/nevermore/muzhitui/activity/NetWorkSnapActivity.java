package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.nevermore.muzhitui.PageLookActivity;
import com.nevermore.muzhitui.R;

import base.BaseActivityTwoV;
import base.view.DragView;
import base.view.VideoGpsView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/12.
 */

public class NetWorkSnapActivity extends BaseActivityTwoV{

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.dragview)
    DragView dragview;

    @Override
    public void init() {
        showBack();
        setMyTitle("网页截图");

        final VideoGpsView videoGpsView = new VideoGpsView(this,7);
        dragview.addDragView(videoGpsView, 550, 850, true, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoGpsView.onClick(v);
            }
        });
    }

    @Override
    public int createSuccessView() {
        return R.layout.activity_network_snap;
    }

    @OnClick(R.id.btn_snap)
    public void onClick(View view){
        Intent intent = new Intent(getApplicationContext(), PageLookActivity.class);
        intent.putExtra(PageLookActivity.IS_SNAP,true);
        intent.putExtra(PageLookActivity.KEY_URL,et_content.getText().toString().trim());
        startActivity(intent);
    }
}
