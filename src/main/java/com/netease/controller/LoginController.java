package com.netease.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.netease.beans.DBBean;
import com.netease.dao.LoginDao;

@Controller
public class LoginController {
	
	//返回值为String类型的对应一个ftl模版html界面
	@RequestMapping(path = "/login")
	public  String login(
			@RequestParam String name, 
			@RequestParam String password, 
			@RequestParam String type , 
			HttpServletRequest request,
			ModelMap model) {
		DBBean.InitialDBConnect();
		LoginDao loginDao = DBBean.context.getBean("LoginDao", LoginDao.class);
		int id=0;
		
		loginDao.initialize(name, password);
		
		//登录过程要初始化session在服务器保存用户这次会话信息
		//by yansong 3.10
		HttpSession session = request.getSession();
		
		model.addAttribute("username", name);
		model.addAttribute("type", type);
		model.addAttribute("password", password);
		
		if(type.equals("buyer")){
			id = loginDao.loginBuyer();
			if(id==0){
				return "loginError";
			}
			//session会保存下列三条键值对，方便会话中随时获取访问的用户信息。
			session.setAttribute("userid", id);
			session.setAttribute("username", name);
			session.setAttribute("type", "buyer");
		}
		else
		{
			id = loginDao.loginSeller();
			if(id==0){
				return "loginError";
			}
			session.setAttribute("userid", id);
			session.setAttribute("username", name);
			session.setAttribute("type", "seller");
		}

		return "loginSuccess";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/login.html";
		
	}
	
}
