package com.netease.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.beans.DBBean;
import com.netease.beans.boughtlistItem;
import com.netease.beans.cartItem;
import com.netease.beans.item;
import com.netease.dao.BuyerBehaviorDao;
import com.netease.dao.SellerBehaviorDao;

@Controller
public class ShowItemsOnSale {
	private BuyerBehaviorDao buyerDao  = null;
	private SellerBehaviorDao sellerDao = null;
	
	private void initializeDao(){
		DBBean.InitialDBConnect();
		buyerDao = DBBean.context.getBean("BuyerBehaviorDao", BuyerBehaviorDao.class);
		sellerDao = DBBean.context.getBean("SellerBehaviorDao", SellerBehaviorDao.class);
	}
	
//�����չʾ���г��۵���Ʒ
	/**
	 * ��Ӧ��ҳչʾҳ��
	 * ���session �ᱣ��id��username��type��������
	 * չʾ������Ʒ����ӦBuyerBehaviorDao��selectAllItems()����
	 */
	@RequestMapping("/items")
	public String showItemsOnSale(HttpServletRequest request,ModelMap model){
		if(buyerDao == null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session==null){
			//sessionΪnull��ʾû���û���¼
			List<item> items = buyerDao.selectAllItems();
			//���õ���items ��list��ʽ����modelmap֮��
			model.addAttribute("otheritemlist",new ArrayList<item>());
			model.addAttribute("itemlist", items);		
		}
		
		if(session!=null){
			//��ʱҪ�����������δ������Ϣ
			String type;
			if(session.getAttribute("type")==null){
				List<item> items = buyerDao.selectAllItems();
				//Ĭ�Ͻ�otheritemlist��Ϊ��
				model.addAttribute("otheritemlist",new ArrayList<item>());
				model.addAttribute("itemlist", items);	
			}
			else{//����Attribute--type
				type = (String)session.getAttribute("type");
				model.addAttribute("type", type);
				if(type.equals("buyer")){
			//���ݵ�¼�û������಻ͬ�趨��ͬ����ʾ����
					int bid = (Integer)session.getAttribute("userid");
					List<item> boughtItems = buyerDao.selectAllItemsBought(bid);
					List<item> unboughtItems = buyerDao.selectAllItemsNotBought(bid);
					
					model.addAttribute("itemlist", unboughtItems);
					//Ĭ��itemlist��δ����Ķ����������б�ǩ
					model.addAttribute("otheritemlist", boughtItems);
				}
				else{
					//type=="seller",��ʱչʾ������������Ϣ
					int sid = (Integer)session.getAttribute("userid");
					List<item> soldItems = sellerDao.selectAllSoldItems(sid);
					List<item> unsoldItems = sellerDao.selectAllUnsoldItems(sid);
					//���õ���items ��list��ʽ����modelmap֮��
					model.addAttribute("itemlist", unsoldItems);
					model.addAttribute("otheritemlist", soldItems);
				}
			}
			
		}
		
		return "showItems";
	}
	
	/**
	 * ��Ӧ��ҳδ�����ǩ
	 */
	@RequestMapping("/itemsunbought")
	public String boughtItems(HttpServletRequest request, ModelMap model){
		if(buyerDao == null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("userid")==null){
			return "loginUnvalidate";
		}
		int bid = (Integer) session.getAttribute("userid");
		
		List<item> boughtItems = new ArrayList<item>();
		List<item> unboughtItems = buyerDao.selectAllItemsNotBought(bid);
		
		model.addAttribute("itemlist", unboughtItems);
		//Ĭ��itemlist��δ����Ķ����������б�ǩ
		model.addAttribute("otheritemlist", boughtItems);
		
		return "showItems";
	}
	
	/**
	 * ��ӦBuyerBehaviorDao��selectItemById()�������ҵ�����id����Ʒ
	 * @param id
	 * @param model
	 * @return detailItem.ftlģ�����չʾ������Ʒ
	 */
	
	
	@RequestMapping("/show")
	public String showItemDetail(@RequestParam  int id,ModelMap model){
		if(buyerDao == null){
			initializeDao();
		}
		item it = buyerDao.selectItemById(id);
		
		model.addAttribute("id", it.getId());
		model.addAttribute("name",it.getIname());
		model.addAttribute("abs",it.getAbs());
		model.addAttribute("introduce",it.getIntroduce());
		model.addAttribute("picture", it.getPicture());
		model.addAttribute("price", it.getPrice());
		model.addAttribute("sold", it.getSold());
		model.addAttribute("bought", false);
		
		return "detailItem";
	}
	
	@RequestMapping("/showbought")
	public String showBoughtItemDetail(@RequestParam  int id,ModelMap model){
		if(buyerDao == null){
			initializeDao();
		}
		item it = buyerDao.selectBoughtItemById(id);
		
		model.addAttribute("id", it.getId());
		model.addAttribute("name",it.getIname());
		model.addAttribute("abs",it.getAbs());
		model.addAttribute("introduce",it.getIntroduce());
		model.addAttribute("picture", it.getPicture());
		model.addAttribute("price", it.getPrice());
		model.addAttribute("sold", it.getSold());
		model.addAttribute("bought", true);
		
		return "detailItem";
	}
	
	/**
	 * @throws IOException 
	 * ��Ʒ��ӵ����ﳵ
	 */
	@RequestMapping("/tocart")
	public String addItemsToCart(@RequestParam int iid,@RequestParam int count,
			HttpServletRequest request,Writer writer) throws IOException{
		if(buyerDao == null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		
		int bid = (Integer)session.getAttribute("userid");
		buyerDao.insertItemToCart(iid, bid, count);
		return "redirect:show?id="+iid;
	}
	/**
	 * �鿴���ﳵ��ȫ����Ʒ
	 * ��Ӧ buyerDao�е�selectItemsInCart�����õ�һ��list
	 * @return cart.ftl
	 */
	@RequestMapping("/cart")
	public String showCartItems(ModelMap modelmap,HttpServletRequest request){
		if(buyerDao==null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("userid")==null){
			return "loginUnvalidate";
		}
		int bid = (Integer)session.getAttribute("userid");
		List<cartItem> cartItems = buyerDao.selectItemsInCart(bid);
		
		modelmap.addAttribute("cartItems", cartItems);
		return "cart";
	}
	/**
	 * �ύ������Ϣ����ӦbuyerDao�е�insertItemToBoughtList
	 * �������й��ﳵ�е���Ʒ
	 * @return 
	 */
	@RequestMapping("/buy")
	public String buyItemsInCart(HttpServletRequest request,ModelMap modelmap){
		if(buyerDao==null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("userid")==null){
			return "loginUnvalidate";
		}
		
		int bid = (Integer)session.getAttribute("userid");
		
		buyerDao.insertItemToBoughtList(bid);
		
		List<boughtlistItem> bought = buyerDao.selectAllBoughtItems(bid);
		modelmap.addAttribute("boughtItems", bought);
		
		double sum = 0;
		if(bought.size()!=0){
			for(boughtlistItem item:bought){
				sum+=item.getAmount();
			}
		}
		modelmap.addAttribute("sum", sum);
		
		return "bought";
	}
	
	@RequestMapping("/bought")
	public String boughtItemsList(HttpServletRequest request,ModelMap modelmap){
		if(buyerDao==null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("userid")==null){
			return "loginUnvalidate";
		}
		
		int bid = (Integer)session.getAttribute("userid");
		List<boughtlistItem> bought = buyerDao.selectAllBoughtItems(bid);
		modelmap.addAttribute("boughtItems", bought);
		double sum = 0;
		if(bought.size()!=0){
			for(boughtlistItem item:bought){
				sum+=item.getAmount();
			}
		}
		
		modelmap.addAttribute("sum", sum);
		
		return "bought";
	}

}
