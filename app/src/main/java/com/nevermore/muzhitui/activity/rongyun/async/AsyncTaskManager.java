/*
    ShengDao Android Client, AsyncTaskManager
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.nevermore.muzhitui.activity.rongyun.async;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.rong.eventbus.EventBus;


/**
 * [A brief description]
 *
 *
 **/
public class AsyncTaskManager {

    private static AsyncTaskManager instance;

    /**
     * 构造方法
     * @param context
     */
    private AsyncTaskManager(Context context) {

        EventBus.getDefault().register(this);
    }

    /**
     * [获取AsyncTaskManager实例，单例模式实现]
     * @param context
     * @return
     */
    public static AsyncTaskManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AsyncTaskManager.class) {
                if (instance == null) {
                    instance = new AsyncTaskManager(context);
                }
            }
        }
        return instance;
    }






}
