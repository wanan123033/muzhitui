package com.liucanwen.citylist.model;

import android.graphics.Bitmap;

import com.liucanwen.citylist.widget.ContactItemInterface;

public class CityItem implements ContactItemInterface
{
	private String nickName;
	private String fullName;
	private int id;
	private String headimg;

	private String agent;
	private Bitmap mBitmap;
	private int flag;//手机联系人好友添加的状态

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}

	/**
	 * 我的好友模糊查询
	 * @param id
	 * @param nickName
	 * @param fullName
	 * @param headimg
     * @param agent
     */
	public CityItem(int id, String nickName, String fullName, String headimg, String agent) {
		this.id = id;
		this.nickName = nickName;
		this.fullName = fullName;
		this.agent = agent;
		this.headimg = headimg;
	}

	/**
	 * 城市列表
	 * @param nickName
	 * @param fullName
     */
	public CityItem(String nickName, String fullName)
	{
		super();
		this.nickName = nickName;
		this.setFullName(fullName);
	}

	/**
	 * 手机联系人
	 * @param nickName
	 * @param fullName
	 * @param headimg
     */
	public CityItem(String nickName, String fullName, String headimg,Bitmap bitmap) {
		this.nickName = nickName;
		this.fullName = fullName;
		this.headimg = headimg;
		this.mBitmap=bitmap;
	}

	@Override
	public String getItemForIndex()
	{
		return fullName;
	}

	@Override
	public String getDisplayInfo()
	{
		return nickName;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

}
