package com.nevermore.muzhitui.module.network;
import com.nevermore.muzhitui.module.service.NetWorkService;

import base.network.RetrofitUtil;
import retrofit2.Retrofit;

/**
 * Created by hehe on 2016/5/26.
 */
public class WorkService {

    public static NetWorkService.Work getWorkService() {
        Retrofit retrofitl = RetrofitUtil.getInstance();
        NetWorkService.Work work = retrofitl.create(NetWorkService.Work.class);
        return work;
    }
}
