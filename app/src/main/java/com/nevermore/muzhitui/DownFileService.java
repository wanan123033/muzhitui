package com.nevermore.muzhitui;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/15.
 */

public class DownFileService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public DownFileService() {
        super("download file");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
