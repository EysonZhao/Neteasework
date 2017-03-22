package com.netease.beans;

public class item {
	
	private int iid;
	private int sid;
	private String iname;
	private String abs;
	private String introduce;
	private int sold;
	private double price;
	//仍在考虑如何注入图片
	private String picture;
	
	public item(){
		//default constructor
	}
	
	public item(int id, int sid, String iname,String abs, String introduce,  int sold, double price, String picture) {
		this.iid = id;
		this.sid = sid;
		this.iname = iname;
		this.abs=abs;
		this.introduce = introduce;
		this.sold = sold;
		this.price = price;
		this.picture=picture;
	}



	@Override
	public String toString() {
		return "item [iid=" + iid + ", sid=" + sid + ", iname=" + iname + ", abs=" + abs + ", introduce=" + introduce
				+ ", sold=" + sold + ", price=" + price + ", picture=" + picture + "]";
	}

	public int getId() {
		return iid;
	}
	public void setId(int id) {
		this.iid = id;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
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

}
