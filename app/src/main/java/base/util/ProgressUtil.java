package base.util;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by Administrator on 2017/11/8.
 */

public class ProgressUtil {
    static KProgressHUD dialog;
    public static void showDialog(Context context,String text){

        dialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(text)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        dialog.show();
    }

    public static void closeDialog() {
        if (dialog != null){
            dialog.dismiss();
        }
    }
}
