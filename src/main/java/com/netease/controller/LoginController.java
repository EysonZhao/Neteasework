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
	
	//����ֵΪString���͵Ķ�Ӧһ��ftlģ��html����
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
		
		//��¼����Ҫ��ʼ��session�ڷ����������û���λỰ��Ϣ
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
			//session�ᱣ������������ֵ�ԣ�����Ự����ʱ��ȡ���ʵ��û���Ϣ��
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
