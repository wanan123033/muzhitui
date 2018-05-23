package com.nevermore.muzhitui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.adapter.ProvinceAdapter;
import com.nevermore.muzhitui.module.bean.Code;
import com.nevermore.muzhitui.module.bean.MSharePreferences;
import com.nevermore.muzhitui.module.bean.ProvinceModel;
import com.nevermore.muzhitui.module.network.WorkService;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import base.BaseActivityTwoV;
import base.SPUtils;
import base.service.Tools;
import base.service.XmlParserHandler;
import base.view.LoadingAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Simone on 2017/1/18.
 */

public class CityPickerActivity extends BaseActivityTwoV {
    @BindView(R.id.tvProvinceName)
    TextView mTvProvinceName;
    @BindView(R.id.provinceList)
    ListView mProvinceList;
    @BindView(R.id.tvProvinceCity)
    TextView mTvProvinceCity;
    private MSharePreferences sharePreferences;

    private List<ProvinceModel> provinces;
    private ProvinceAdapter provinceAdapter;
    private ProvinceAdapter cityAdapter;


    private LoadingAlertDialog mLoadingAlertDialog;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @OnClick({R.id.tvProvinceName, R.id.tvProvinceCity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvProvinceName:
                loadData(wxProvince, wxCity);
                break;
            case R.id.tvProvinceCity:
                loadData(wxProvince, wxCity);
                break;
        }
    }

    public interface OnCityPikerListener {
        void onCityPicker(String province, String city);
    }

    private int procicePosition;
    private String city;
    private String province;
    private String wxProvince, wxCity = "";

    @Override
    public void init() {
        setMyTitle("地区");
        showBack();
        try {
            mLocationClient = new LocationClient(this); // 声明LocationClient类
            mLocationClient.registerLocationListener(myListener); // 注册监听函数
        } catch (Exception e) {
            e.printStackTrace();
        }
        initLocation();
        mLoadingAlertDialog = new LoadingAlertDialog(this);

        String address = getIntent().getStringExtra("address");
        mTvProvinceCity.setText(address);
        sharePreferences = MSharePreferences.getInstance();
        sharePreferences.getSharedPreferences(CityPickerActivity.this);
        initProvinceDatas();
        initProvince();


        mProvinceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (isCity) {

                    province = provinces.get(position).getName();
                    Log.e("city province:", province);
                    procicePosition = position;
                    cityAdapter = new ProvinceAdapter(CityPickerActivity.this, provinces.get(
                            position).getCityList(), false);
                    mProvinceList.setAdapter(cityAdapter);
                    isCity = false;

                } else {
                    city = provinces.get(procicePosition).getCityList()
                            .get(position).getName();


                    Log.e("city222:", city);
                    loadData(province, city);


                }
            }
        });
    }

    private boolean isCity;

    public void initProvince() {

        provinceAdapter = new ProvinceAdapter(CityPickerActivity.this, provinces);
        mProvinceList.setAdapter(provinceAdapter);
        mProvinceList.setSelection(sharePreferences.getInt(
                Tools.KEY_PROVINCE, 0));
        isCity = true;
    }


    @Override
    public int createSuccessView() {
        return R.layout.province_listview;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }


    public void initProvinceDatas() {

        AssetManager asset = getAssets();

        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinces = handler.getDataList();
            Log.e("provinces", provinces.size() + "'");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void loadData(
            final String wx_province, final String wx_city) {

        mLoadingAlertDialog.show();


        Subscription sbGetCode = wrapObserverWithHttp(WorkService.getWorkService().updateUserInfo((String) SPUtils.get(SPUtils.GET_LOGIN_ID,""),5, "中国", wx_province, wx_city)).subscribe(new Subscriber<Code>() {
            @Override
            public void onCompleted() {
                mLoadingAlertDialog.dismiss();
                removeLoadingView();

            }

            @Override
            public void onError(Throwable e) {

                mLoadingAlertDialog.dismiss();
                removeLoadingView();
                showErrorView();
                e.printStackTrace();
            }

            @Override
            public void onNext(final Code code) {

                if (code.getState().equals("1")) {
                    showTest("修改成功");


                    Intent intent = new Intent();
                    intent.putExtra("address", "中国 • " + wx_province + " • " + wx_city);
                    setResult(Activity.RESULT_OK, intent);


                    finish();
                }
                showTest(code.getMsg());


            }
        });
        addSubscription(sbGetCode);
    }


    /**
     * 设置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();// 开始请求位置


    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                Log.e("location", location.getAddrStr() + location.getCity());

                wxProvince = location.getProvince();
                wxCity = location.getCity();
                mTvProvinceName.setText(location.getCountry() + " • " + location.getProvince() + " • " + location.getCity());
                mLocationClient.stop();
            } else {
                mTvProvinceName.setText("无法定位");
                return;
            }
        }

    }

    public void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();// 关闭定位SDK
            mLocationClient = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListener();//停止监听
    }


}
