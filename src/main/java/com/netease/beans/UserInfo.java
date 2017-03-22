package com.netease.beans;

public class UserInfo {
	private int id;
	private boolean isBuyer;
	public UserInfo(int id, boolean isBuyer) {
		super();
		this.id = id;
		this.isBuyer = isBuyer;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isBuyer() {
		return isBuyer;
	}
	public void setBuyer(boolean isBuyer) {
		this.isBuyer = isBuyer;
	}
	
	

}
