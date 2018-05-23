package com.nevermore.muzhitui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nevermore.muzhitui.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivityTwoV;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class GenQRCodeActivity extends BaseActivityTwoV{

    //BEGIN:VCARD\nFN:%@\nTEL;WORK:%@\nADR;TYPE=WORK:;;%@\nORG:%@\nEND:VCARD  //生成名片二维码用的字符格式
    String string = "BEGIN:VCARD\nFN:%1$s\nTEL;WORK:%2$s\nADR;TYPE=WORK:;;%3$s\nORG:%4$s\nEND:VCARD";

    private List<View> views = new ArrayList<>();

    @BindView(R.id.ll_network)
    LinearLayout ll_network;
    @BindView(R.id.ll_text)
    LinearLayout ll_text;
    @BindView(R.id.ll_mpll)
    LinearLayout ll_mpll;
    @BindView(R.id.ll_mp)
    LinearLayout ll_mp;
    @BindView(R.id.et_net_content)
    EditText et_net_content;
    @BindView(R.id.et_wz_content)
    EditText et_wz_content;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_company)
    EditText et_company;

    private boolean isMpQr;
    private String httpQr,stringQr;
    @Override
    public void init() {
        showBack();
        setMyTitle("生成二维码");

        views.clear();
        views.add(ll_network);
        views.add(ll_text);
        views.add(ll_mpll);
    }

    private int isNet = 0;
    @Override
    public int createSuccessView() {
        return R.layout.activity_gen_qr;
    }

    @OnClick({R.id.ll_network,R.id.ll_text,R.id.ll_mpll,R.id.tv_genQr})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_network:
                isMpQr = false;
                setTitleView(ll_network);
                et_net_content.setVisibility(View.VISIBLE);
                ll_mp.setVisibility(View.GONE);
                et_wz_content.setVisibility(View.GONE);
                isNet = 0;
                break;
            case R.id.ll_text:
                isMpQr = false;
                isNet = 1;
                setTitleView(ll_text);
                et_wz_content.setVisibility(View.VISIBLE);
                ll_mp.setVisibility(View.GONE);
                et_net_content.setVisibility(View.GONE);
                et_wz_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length() > 200){
                            et_wz_content.setText(s.subSequence(0,200));
                            et_wz_content.setSelection(200);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case R.id.ll_mpll:
                isMpQr = true;
                setTitleView(ll_mpll);
                et_wz_content.setVisibility(View.GONE);
                et_net_content.setVisibility(View.GONE);
                ll_mp.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_genQr:
                genQr();
                break;
        }
    }

    private void genQr() {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String company = et_company.getText().toString().trim();
        String net_content = et_net_content.getText().toString().trim();
        String wz_content = et_wz_content.getText().toString().trim();
        if (isMpQr) {
            Intent intent = new Intent(getApplicationContext(), StringGenQRActivity.class);
            intent.putExtra(StringGenQRActivity.QRStr, String.format(string, name, phone, address, company));
            startActivity(intent);
        }else {
            if (isNet == 1) {
                Intent intent = new Intent(getApplicationContext(), StringGenQRActivity.class);
                intent.putExtra(StringGenQRActivity.QRStr, wz_content);
                startActivity(intent);
            }else if (isNet == 0){
                Intent intent = new Intent(getApplicationContext(), StringGenQRActivity.class);
                intent.putExtra(StringGenQRActivity.QRStr, net_content);
                startActivity(intent);
            }
        }
    }

    private void setTitleView(View view) {
        for (View child : views){
            if (child == view){
                child.setBackgroundResource(R.drawable.shape_btn_green_bg);
            }else {
                child.setBackgroundResource(R.drawable.shape_btn_white_bg);
            }
        }
    }
}
