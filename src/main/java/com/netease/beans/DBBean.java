package com.netease.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.dao.SellerBehaviorDao;

public class DBBean {
	
	public static ApplicationContext context=null;
	
	public static SellerBehaviorDao InitialDBConnect(){
		context = new ClassPathXmlApplicationContext("application-context.xml");
		return context.getBean("SellerBehaviorDao", SellerBehaviorDao.class);
	}
}
