package com.nevermore.muzhitui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2017/7/11.
 *
 */

public class CallPhonePopintWindow extends PopupWindow {
    public CallPhonePopintWindow(final Context context, final String phone){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pw_callphone,null,false);
        setContentView(view);

        view.findViewById(R.id.btnCallphone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                context.startActivity(intent);
            }
        });

        view.findViewById(R.id.btndimiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
