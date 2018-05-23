package com.nevermore.muzhitui.daoservices;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nevermore.muzhitui.module.bean.DynamicBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class DynamicServiceDao {
    private static DynamicServiceDao instance;
    private String tableName = "dynamic";


    private SQLiteDatabase database;
    private DynamicServiceDao(SQLiteOpenHelper helper){

        database = helper.getWritableDatabase();
    }

    public static synchronized DynamicServiceDao getInstance(SQLiteOpenHelper helper) {
        if (instance == null){
            instance = new DynamicServiceDao(helper);
        }
        return instance;
    }

    /**
     * 保存一页动态数据
     * @param data
     */
    public void saveAll(List<DynamicBean.Dynamic> data){
        database.beginTransaction();
        for(int i = 0 ; i < data.size() ; i++){
            DynamicBean.Dynamic dynamic = data.get(i);
            ContentValues values = new ContentValues();
            values.put("id",dynamic.getId());
            database.insert(tableName,null,values);
        }
        database.endTransaction();

    }
}
