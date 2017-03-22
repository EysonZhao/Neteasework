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
	
//对买家展示所有出售的物品
	/**
	 * 对应主页展示页面
	 * 这里，session 会保存id，username，type三种属性
	 * 展示所有物品，对应BuyerBehaviorDao中selectAllItems()方法
	 */
	@RequestMapping("/items")
	public String showItemsOnSale(HttpServletRequest request,ModelMap model){
		if(buyerDao == null){
			initializeDao();
		}
		HttpSession session = request.getSession();
		if(session==null){
			//session为null表示没有用户登录
			List<item> items = buyerDao.selectAllItems();
			//将得到的items 以list形式放入modelmap之中
			model.addAttribute("otheritemlist",new ArrayList<item>());
			model.addAttribute("itemlist", items);		
		}
		
		if(session!=null){
			//此时要分类存放已买和未购买信息
			String type;
			if(session.getAttribute("type")==null){
				List<item> items = buyerDao.selectAllItems();
				//默认将otheritemlist置为空
				model.addAttribute("otheritemlist",new ArrayList<item>());
				model.addAttribute("itemlist", items);	
			}
			else{//存在Attribute--type
				type = (String)session.getAttribute("type");
				model.addAttribute("type", type);
				if(type.equals("buyer")){
			//根据登录用户的种类不同设定不同的显示内容
					int bid = (Integer)session.getAttribute("userid");
					List<item> boughtItems = buyerDao.selectAllItemsBought(bid);
					List<item> unboughtItems = buyerDao.selectAllItemsNotBought(bid);
					
					model.addAttribute("itemlist", unboughtItems);
					//默认itemlist是未购买的东西，不含有标签
					model.addAttribute("otheritemlist", boughtItems);
				}
				else{
					//type=="seller",此时展示卖家卖出的信息
					int sid = (Integer)session.getAttribute("userid");
					List<item> soldItems = sellerDao.selectAllSoldItems(sid);
					List<item> unsoldItems = sellerDao.selectAllUnsoldItems(sid);
					//将得到的items 以list形式放入modelmap之中
					model.addAttribute("itemlist", unsoldItems);
					model.addAttribute("otheritemlist", soldItems);
				}
			}
			
		}
		
		return "showItems";
	}
	
	/**
	 * 对应主页未购买标签
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
		//默认itemlist是未购买的东西，不含有标签
		model.addAttribute("otheritemlist", boughtItems);
		
		return "showItems";
	}
	
	/**
	 * 对应BuyerBehaviorDao中selectItemById()方法，找到单个id的商品
	 * @param id
	 * @param model
	 * @return detailItem.ftl模版界面展示单个商品
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
	 * 商品添加到购物车
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
	 * 查看购物车中全部商品
	 * 对应 buyerDao中的selectItemsInCart方法得到一个list
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
	 * 提交订单信息，对应buyerDao中的insertItemToBoughtList
	 * 清算所有购物车中的商品
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
