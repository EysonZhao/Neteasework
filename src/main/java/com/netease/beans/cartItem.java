package com.netease.beans;

public class cartItem {
	private int bid;
	private int iid;
	private int count;
	private String iname;
	private double price;
	private String picture;
	private double amount = count* price;
	
	
	public cartItem(int bid, int iid, int count, String iname, double price, String picture) {
		super();
		this.bid = bid;
		this.iid = iid;
		this.count = count;
		this.iname = iname;
		this.price = price;
		this.picture = picture;
		setAmount();
	}
	
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount() {
		this.amount = price * count;
	}
	@Override
	public String toString() {
		return "cartItem [bid=" + bid + ", iid=" + iid + ", count=" + count + ", iname=" + iname + ", price=" + price
				+ ", picture=" + picture + ", amount=" + amount + "]";
	}
	
}
