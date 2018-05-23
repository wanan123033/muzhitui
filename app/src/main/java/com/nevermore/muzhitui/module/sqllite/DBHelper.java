package com.nevermore.muzhitui.module.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME="userinfo.db";
	private static final int DATABASE_VERSION=1;
	public static final String	TABLE_NAME_All="userinforong";
	public static final String	TABLE_NAME_FRIENDS="friends";
	public static final String	TABLE_NAME_LEVERLS="levels";
	//CursorFactory设置为null,使用默认值
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 发送给融云的我的好友信息
		db.execSQL("CREATE TABLE IF NOT EXISTS userinforong" +
				"(id INTEGER , user_name VARCHAR, headimg VARCHAR, agent TEXT)");
		// 显示在的我的好友列表的信息
		db.execSQL("CREATE TABLE IF NOT EXISTS friends" +
				"(id INTEGER , user_name VARCHAR, headimg VARCHAR, agent TEXT)");
		// 显示在的我的下级列表的信息
		db.execSQL("CREATE TABLE IF NOT EXISTS levels" +
				"(id INTEGER , user_name VARCHAR, headimg VARCHAR, agent TEXT)");

	}
	//如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE userinforong ADD COLUMN other STRING");//所有好友信息包括我的好友+下级
		db.execSQL("ALTER TABLE friends ADD COLUMN other STRING");//我的好友
		db.execSQL("ALTER TABLE levels ADD COLUMN other STRING");//下级
	}

}