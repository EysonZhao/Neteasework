package com.netease.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class TestPageController {

	@RequestMapping("/test")
	public String showMainPage(HttpServletRequest request,Writer writer) throws IOException {
		HttpSession session = request.getSession();
		int userid = (Integer) session.getAttribute("userid");
		String username = (String) session.getAttribute("username");
		String type = (String) session.getAttribute("type");
		writer.write("####"+userid +"####   " );
		writer.write("####"+username +"####   " );
		writer.write("####"+type +"####   " );
		return "test";

	}
}
