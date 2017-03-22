package com.netease.beans;

public class boughtlistItem {
	private int bid;
	private int iid;
	private String biname;
	private int num;
	private String time;
	private double bprice;
	private String picture;
	private double amount;
	
	public boughtlistItem(int bid, int iid, String biname, int num, String time, double bprice,String picture) {
		super();
		this.bid = bid;
		this.iid = iid;
		this.biname = biname;
		this.num = num;
		this.time = time;
		this.bprice = bprice;
		this.picture=picture;
		this.amount=bprice*num;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
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
	public String getBiname() {
		return biname;
	}
	public void setBiname(String biname) {
		this.biname = biname;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getBprice() {
		return bprice;
	}
	public void setBprice(double bprice) {
		this.bprice = bprice;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "boughtlistItem [bid=" + bid + ", iid=" + iid + ", biname=" + biname + ", num=" + num + ", time=" + time
				+ ", bprice=" + bprice + "]";
	}
	
	

}
