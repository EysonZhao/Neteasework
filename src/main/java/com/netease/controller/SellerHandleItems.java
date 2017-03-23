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

	// ������1����һ��ʹ�ã���ʼ����db������
	private void initializeDao() {
		DBBean.InitialDBConnect();
		sellerDao = DBBean.context.getBean("SellerBehaviorDao", SellerBehaviorDao.class);
	}

	// ������2������utils���еĽ��빤����������ķ��ؿ��õ�String
	private String param2Str(String str) {
		return EncodeString.encodeStr(str);
	}
	
	//������3�������û��Ƿ��¼��bean�Ƿ��ʼ�����
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
	 * ��ʼ����Ӧ�½����ܣ���Ӧ����editItem�ĳ�ʼ������
	 * @param modelmap,request
	 * @return "editItem" �༭��Ʒ����
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
	 * �����½�һ����Ʒ
	 * 
	 * @param iname,abs,introduce,
	 *            price, modelmap,request , picture
	 * @return items  ��ʼ��Ʒ����
	 *         ֵ��ע����ǣ������漰���˴�ǰ�˴������ݣ������Ҫ��javaĬ�ϱ���(unicode)ǿ��ת���ɱ���Ŀʹ�õ�UTF-8
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
		// post����Ŀ��������ĵ�String��������Ҫ����: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);

		double price_s = Double.parseDouble(price);
		// price�ĸ�ʽ�淶����ǰ�˿���

		modelmap.addAttribute("modify", false);
		sellerDao.insertAnItemBySeller(sid, iname_s, abs_s, introduce_s, price_s, picture);
		return "redirect:items";
	}
	/**************************************************************************
	 * �ϴ�ͼƬ����һ����Ʒ��¼
	 * @param iname, abs, introduce,price,file,modelmap,request
	 * @return "items"����
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
		// ���û���¼���ߵ�¼����˻������ش������

		int sid = (Integer) session.getAttribute("userid");
		// post����Ŀ��������ĵ�String��������Ҫ����: iname,abs,introduce
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
			
			System.out.println("�ļ��ϴ��ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("�ļ��ϴ�ʧ�ܣ�");
		}
		
		sellerDao.insertAnItemBySeller(sid, iname_s, abs_s, introduce_s, price_s, url+filename);
		
		return "redirect:items";
	}

	/**************************************************************************
	 * ��ʼ��������Ʒ���棬���ر༭��Ʒ����
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
		// �˴�Ӧ����modelmap���õ�item��Ϣ
		modelmap.addAttribute("name", it.getIname());
		modelmap.addAttribute("abs", it.getAbs());
		modelmap.addAttribute("introduce", it.getIntroduce());
		modelmap.addAttribute("price", it.getPrice());
		modelmap.addAttribute("picture", it.getPicture());

		return "editItem";
	}

	/**************************************************************************
	 * �޸�һ����Ʒ��Ϣ
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

		// post����Ŀ��������ĵ�String��������Ҫ����: iname,abs,introduce
		String iname_s = param2Str(iname);
		String abs_s = param2Str(abs);
		String introduce_s = param2Str(introduce);
		int iid_s = Integer.parseInt(iid);
		double price_s = Double.parseDouble(price);

		sellerDao.updateItemBySeller(iid_s, iname_s, abs_s, introduce_s, price_s, picture);

		return "redirect:show?id=" + iid;

	}
	
	/**************************************************************************
	 * �ϴ�ͼƬ�޸���Ʒ����
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

		// post����Ŀ��������ĵ�String��������Ҫ����: iname,abs,introduce
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
			
			System.out.println("�ļ��ϴ��ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("�ļ��ϴ�ʧ�ܣ�");
		}

		sellerDao.updateItemBySeller(iid_s, iname_s, abs_s, introduce_s, price_s, url+filename);

		return "redirect:show?id=" + iid;
	}
	
	/*********************************************************************
	 * ɾ��һ��δ���۵���Ʒ
	 * @param id
	 * @return "items"��Ʒ����
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
