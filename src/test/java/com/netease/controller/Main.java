package com.netease.controller;

import java.io.IOException;

import com.netease.beans.DBBean;
import com.netease.dao.BuyerBehaviorDao;
import com.netease.dao.SellerBehaviorDao;

public class Main {

	public static void main(String[] args) throws IOException {
//		get Database connected
		DBBean.InitialDBConnect();	
		SellerBehaviorDao bean = DBBean.context.getBean("SellerBehaviorDao", SellerBehaviorDao.class);
		BuyerBehaviorDao buyer = DBBean.context.getBean("BuyerBehaviorDao",BuyerBehaviorDao.class);
		
		buyer.insertItemToBoughtList(1);
		
		bean.resetDB();

        String path = SellerHandleItems.class.getResource("").getPath().substring(1).split("target")[0];
        System.out.println(path);
        
        
        System.out.println( System.getProperty("user.dir")); 
	}
	

}
