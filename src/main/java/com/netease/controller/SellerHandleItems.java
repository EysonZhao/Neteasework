package com.netease.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.netease.beans.DBBean;
import com.netease.beans.item;
import com.netease.dao.SellerBehaviorDao;
import com.netease.utils.EncodeString;
import com.netease.utils.FileUploadHelper;

@Controller
public class SellerHandleItems {
	private SellerBehaviorDao sellerDao = null;

	// 工具类1：第一次使用，初始化和db的连接
	private void initializeDao() {
		DBBean.InitialDBConnect();
		sellerDao = DBBean.context.getBean("SellerBehaviorDao", SellerBehaviorDao.class);
	}

	// 工具类2：调用utils包中的解码工具类解码中文返回可用的String
	private String param2Str(String str) {
		return EncodeString.encodeStr(str);
	}
	
	//工具类3：检验用户是否登录，bean是否初始化完毕
	private boolean testUserAuthority(HttpSession session){
		if (sellerDao == null) {
			initializeDao();
		}
		if(session.getAttribute("type")==null||session.getAttribute("type")=="buyer"){
			return false;
		}
		return true;
		
	}
	/**
	 * 初始化响应新建功能，对应操作editItem的初始化工作
	 * @param modelmap,request
	 * @return "editItem" 编辑商品界面
	 */

	@RequestMapping("/new")
	public String newAnItem(ModelMap modelmap, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}
		modelmap.addAttribute("modify", false);
		return "editItem";
	}

	/**
	 * 卖家新建一个商品
	 * 
	 * @param iname,abs,introduce,
	 *            price, modelmap,request , picture
	 * @return items  初始物品界面
	 *         值得注意的是，这里涉及到了从前端传入数据，因此需要将java默认编码(unicode)强制转换成本项目使用的UTF-8
	 */
	@RequestMapping(path = "/insertitem", method = RequestMethod.POST)
	public String insertAnItem(@RequestParam String iname, 
			@RequestParam String abs, 
			@RequestParam String introduce,
			@RequestParam String price, 
			@RequestParam String picture, 
			ModelMap modelmap, 
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}

		int sid = (Integer) session.getAttribute("userid");
		// post传入的可能有中文的String参数均需要解码: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);

		double price_s = Double.parseDouble(price);
		// price的格式规范交给前端控制

		modelmap.addAttribute("modify", false);
		sellerDao.insertAnItemBySeller(sid, iname_s, abs_s, introduce_s, price_s, picture);
		return "redirect:items";
	}
	/**************************************************************************
	 * 上传图片插入一条商品记录
	 * @param iname, abs, introduce,price,file,modelmap,request
	 * @return "items"界面
	 */
	@RequestMapping(path = "/insertitemwithimg", method = RequestMethod.POST)
	public String insertAnItem(@RequestParam String iname, 
			@RequestParam String abs, 
			@RequestParam String introduce,
			@RequestParam String price, 
			@RequestParam CommonsMultipartFile file, 
			ModelMap modelmap, 
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}
		// 无用户登录或者登录买家账户，返回错误界面

		int sid = (Integer) session.getAttribute("userid");
		// post传入的可能有中文的String参数均需要解码: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);

		double price_s = Double.parseDouble(price);
		
		modelmap.addAttribute("modify", false);
		
		String path = request.getSession().getServletContext().getRealPath("/")+ "img\\";

		String url1 = request.getLocalAddr();;
		String url2 = request.getContextPath();

		String url = "http://"+url1+":8080"+url2+"/img/";
		String filename = "";
		try {
			System.out.println(path);
			File target = new File(path);
			filename = FileUploadHelper.upload(file, target);
			
			System.out.println("文件上传成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("文件上传失败！");
		}
		
		sellerDao.insertAnItemBySeller(sid, iname_s, abs_s, introduce_s, price_s, url+filename);
		
		return "redirect:items";
	}

	/**************************************************************************
	 * 初始化更改商品界面，返回编辑商品界面
	 * @param modelmap
	 * @return 
	 */
	@RequestMapping("/edit")
	public String modifyAnItem(ModelMap modelmap,@RequestParam int id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}
		modelmap.addAttribute("modify", true);
		modelmap.addAttribute("id", id);

		item it = sellerDao.selectItemById(id);
		// 此处应更新modelmap，得到item信息
		modelmap.addAttribute("name", it.getIname());
		modelmap.addAttribute("abs", it.getAbs());
		modelmap.addAttribute("introduce", it.getIntroduce());
		modelmap.addAttribute("price", it.getPrice());
		modelmap.addAttribute("picture", it.getPicture());

		return "editItem";
	}

	/**************************************************************************
	 * 修改一个商品信息
	 * @return
	 */
	@RequestMapping(path = "/modifyitem", method = RequestMethod.POST)
	public String modifyAnItem(@RequestParam String iid,
			@RequestParam String iname, 
			@RequestParam String abs,
			@RequestParam String introduce, 
			@RequestParam String price, 
			@RequestParam String picture, ModelMap modelmap,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}

		// post传入的可能有中文的String参数均需要解码: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);
		int iid_s = Integer.parseInt(iid);
		double price_s = Double.parseDouble(price);

		sellerDao.updateItemBySeller(iid_s, iname_s, abs_s, introduce_s, price_s, picture);

		return "redirect:show?id=" + iid;

	}
	
	/**************************************************************************
	 * 上传图片修改商品界面
	 * @param iid,iname,abs,introduce,price,file,modelmap,request
	 * @return
	 */
	@RequestMapping(path = "/modifyitemwithimg", method = RequestMethod.POST)
	public String modifyAnItem(@RequestParam String iid,
			@RequestParam String iname, 
			@RequestParam String abs,
			@RequestParam String introduce, 
			@RequestParam String price, 
			@RequestParam CommonsMultipartFile file,
			ModelMap modelmap,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}

		// post传入的可能有中文的String参数均需要解码: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);
		int iid_s = Integer.parseInt(iid);
		double price_s = Double.parseDouble(price);
		
		String path = request.getSession().getServletContext().getRealPath("/")+ "img\\";
		System.out.println(path);

		String url1 = request.getLocalAddr();
		String url2 = request.getContextPath();
		
		String url = "http://"+url1+":8080"+url2+"/img/";
		String filename = "";
		
		try {
			System.out.println(path);
			File target = new File(path);
			filename=FileUploadHelper.upload(file, target);
			
			System.out.println("文件上传成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("文件上传失败！");
		}

		sellerDao.updateItemBySeller(iid_s, iname_s, abs_s, introduce_s, price_s, url+filename);

		return "redirect:show?id=" + iid;
	}
	
	/*********************************************************************
	 * 删除一个未出售的商品
	 * @param id
	 * @return "items"商品界面
	 */
	@RequestMapping("/delete")
	public String deleteUnsoldItem(@RequestParam int id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!testUserAuthority(session)) {
			return "loginUnvalidate";
		}
		sellerDao.deleteItemBySeller(id);
		return "redirect:items";
	}

}
