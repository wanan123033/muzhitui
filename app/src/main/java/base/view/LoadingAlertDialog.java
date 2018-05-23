package base.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.fragment.TextAlignFragment;

import base.MyLogger;
import base.UIUtils;

/**
 * Created by hehe on 2016/5/27.
 */
public class LoadingAlertDialog {
    private AlertDialog dialog;

    ImageView mIvLoading;

    private final Animation mOperatingAnim;

    public LoadingAlertDialog(Activity activity) {
        dialog = new AlertDialog.Builder(activity).create();  //内存泄露的危险
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        /**设置在加载时是否可点击返回键，点击返回键是否有效**/
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    mIvLoading.clearAnimation();
                }
                return false;
            }
        });
        View view = UIUtils.inflate(activity, R.layout.dialog_loading);
        dialog.setView(view);
        mIvLoading = (ImageView) view.findViewById(R.id.ivLoading);
        mOperatingAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        mOperatingAnim.setInterpolator(lin);
        dialog.setCancelable(false);//设置点击对话框页面动画不消失，等加载完成后关闭

    }

    public void show() {
        if (mOperatingAnim != null) {
            mIvLoading.startAnimation(mOperatingAnim);
        }
        dialog.show();
    }


    public void dismiss() {
        dialog.dismiss();
        mIvLoading.clearAnimation();
    }

    public void setText(String text) {
        //tv_name.setText(text);
    }
}
