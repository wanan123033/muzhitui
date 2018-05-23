package com.nevermore.muzhitui.module.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
private Context mContext;
	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里

		db = helper.getWritableDatabase();
		mContext=context;
	}

	/**
	 * add userInfoRongs 我所有的好友包括好友和下级
	 *
	 * @param userInfoRongs userinforong
	 */
	public void add(List<UserInfoRong> userInfoRongs) {
		db.beginTransaction();// 开始事物
		try {
			for (UserInfoRong userInfoRong : userInfoRongs) {
				db.execSQL("INSERT INTO userinforong VALUES(?,?, ?, ?)",
						new Object[] {userInfoRong.id, userInfoRong.user_name, userInfoRong.headimg, userInfoRong.agent });

			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	/**
	 * add userInfoRongs 我的好友
	 *
	 * @param userInfoRongs friends
	 */
	public void addFriend(List<UserInfoRong> userInfoRongs) {
		db.beginTransaction();// 开始事物
		try {
			for (UserInfoRong userInfoRong : userInfoRongs) {
					db.execSQL("INSERT INTO friends VALUES(?,?, ?, ?)",
						new Object[] {userInfoRong.id, userInfoRong.user_name, userInfoRong.headimg, userInfoRong.agent });
				Log.e("======friends====","friends");
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 我的下级
	 * @param userInfoRongs levels
     */
	public void addLevels(List<UserInfoRong> userInfoRongs) {
		db.beginTransaction();// 开始事物
		try {
			for (UserInfoRong userInfoRong : userInfoRongs) {
				db.execSQL("INSERT INTO levels VALUES(?,?, ?, ?)",
						new Object[] {userInfoRong.id, userInfoRong.user_name, userInfoRong.headimg, userInfoRong.agent });
				Log.e("======level====","levels");
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
//
	/***
	 * update UserInfoRong
	 *
	 * @param userInfoRongs
	 */

	public void updateUserInfo(UserInfoRong userInfoRongs) {
		ContentValues cv = new ContentValues();
		cv.put("user_name", userInfoRongs.user_name);
		cv.put("headimg", userInfoRongs.headimg);
		cv.put("agent", userInfoRongs.agent);
		db.update("userinforong", cv, "id=?", new String[] { String.valueOf(userInfoRongs.id )});
		db.update("friends", cv, "id=?", new String[] { String.valueOf(userInfoRongs.id )});
	}

	/**
	 * 根据id删除对象信息
	 * @param userInfoRongs
	 * @param
     */
	public void deleteOldUserInfo(UserInfoRong userInfoRongs) {

			db.delete("userinforong", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });

			db.delete("friends", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });

			db.delete("levels", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });



	}
	/**
	 * 根据id删除对象信息
	 * @param userInfoRongs
	 * @param state
	 */
	public void deleteOldUserInfo(UserInfoRong userInfoRongs,String state) {
		if (state.equals("userinforong")){
			db.delete("userinforong", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });
		}else if (state.equals("friends")){
			db.delete("friends", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });
		}else if (state.equals("levels")){
			db.delete("levels", "id = ?",
					new String[] { String.valueOf(userInfoRongs.id) });
		}


	}
	/**
	 * 删除数据库
	 *
	 * @DBName 数据库名字
	 */
	public boolean deleteDBByName() {
		mContext.deleteDatabase(DBHelper.DATABASE_NAME);
		Log.d("DB", "had deleted database: " +DBHelper.DATABASE_NAME);
		return false;
	}
	/**
	 * 删除数据库中表的数据 我的好友+我的下级好友
	 * @DBName 数据库名字
	 * @TableName 表名字
	 */
	public boolean deleteTableByDBNameAll() {

		db.delete(DBHelper.TABLE_NAME_All, null, null);
		db.delete(DBHelper.TABLE_NAME_FRIENDS, null, null);
		db.delete(DBHelper.TABLE_NAME_LEVERLS, null, null);
		db.close();

		Log.d("DB", "had deleted table:");
		return false;
	}

	/**
	 * 我的好友表删除
	 * @return
     */
	public boolean deleteTableByDBNameAllAndFriends() {
		db.delete(DBHelper.TABLE_NAME_All, null, null);
		db.delete(DBHelper.TABLE_NAME_FRIENDS, null, null);


		Log.d("DB", "had deleted table:");
		return false;
	}
	/**
	 * 我的好友表删除
	 * @return
	 */
	public boolean deleteTableByDBNameAllAndLever() {
		db.delete(DBHelper.TABLE_NAME_All, null, null);
		db.delete(DBHelper.TABLE_NAME_LEVERLS, null, null);


		Log.d("DB", "had deleted table:");
		return false;
	}

	/***
	 * query UserInfoRong 查询我的好友和我的下级
	 *
	 * @return
	 */
	public List<UserInfoRong> query() {
		ArrayList<UserInfoRong> persons = new ArrayList<UserInfoRong>();

			Cursor c = queryTheCursor();


		while (c.moveToNext()) {
			UserInfoRong person = new UserInfoRong();
			person.id = c.getInt(c.getColumnIndex("id"));
			person.user_name = c.getString(c.getColumnIndex("user_name"));
			person.headimg = c.getString(c.getColumnIndex("headimg"));
			person.agent = c.getString(c.getColumnIndex("agent"));
			persons.add(person);
		}
		c.close();
		return persons;
	}

	/**
	 * query all persons, return cursor
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM userinforong", null);
		return c;
	}
	/***
	 * query UserInfoRong 查询我的好友
	 *
	 * @return
	 */
	public List<UserInfoRong> queryFriends() {
		ArrayList<UserInfoRong> persons = new ArrayList<UserInfoRong>();

		Cursor c = queryTheCursorFriends();


		while (c.moveToNext()) {
			UserInfoRong person = new UserInfoRong();
			person.id = c.getInt(c.getColumnIndex("id"));
			person.user_name = c.getString(c.getColumnIndex("user_name"));
			person.headimg = c.getString(c.getColumnIndex("headimg"));
			person.agent = c.getString(c.getColumnIndex("agent"));
			persons.add(person);
		}
		c.close();
		return persons;
	}

	/**
	 * query all persons, return cursor
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursorFriends() {
		Cursor c = db.rawQuery("SELECT * FROM friends", null);
		return c;
	}
	/***
	 * query UserInfoRong 查询我的下级好友
	 *
	 * @return
	 */
	public List<UserInfoRong> queryLevels() {
		ArrayList<UserInfoRong> persons = new ArrayList<UserInfoRong>();

		Cursor c = queryTheCursorLevels();


		while (c.moveToNext()) {
			UserInfoRong person = new UserInfoRong();
			person.id = c.getInt(c.getColumnIndex("id"));
			person.user_name = c.getString(c.getColumnIndex("user_name"));
			person.headimg = c.getString(c.getColumnIndex("headimg"));
			person.agent = c.getString(c.getColumnIndex("agent"));
			persons.add(person);
		}
		c.close();
		return persons;
	}

	/**
	 * query all persons, return cursor
	 *
	 * @return Cursor
	 */
	public Cursor queryTheCursorLevels() {
		Cursor c = db.rawQuery("SELECT * FROM levels", null);
		return c;
	}
	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}