package com.netease.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.netease.RowMapper.boughtlistRowMapper;
import com.netease.RowMapper.cartItemRowMapper;
import com.netease.RowMapper.itemRowMapper;
import com.netease.beans.boughtlistItem;
import com.netease.beans.cartItem;
import com.netease.beans.item;

@Repository
public class BuyerBehaviorDao {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private Map<String,Object> paramMap = new  HashMap<String,Object>();
	
	@Autowired
	public void setDataSource(BasicDataSource datasource){
		jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}
	//#########################################################
	//             显示物品
	/**1
	 * 未登录状态默认显示全部卖家的全部商品
	 */
	public List<item> selectAllItems(){
		String sql = "select * from item";
		return this.jdbcTemplate.query(sql, new itemRowMapper());
	}
	/**2
	 * 查看界面下，买家查看一个未购买商品
	 */
	public item selectItemById(int iid){
		String sql = "select * from item where iid = :iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	/**3
	 *  查看界面下，买家查看一个已购买商品
	 *  唯一不同之处在于，已购买商品的价格需要去boughtlist已购买列表中获取。
	 */
	public item selectBoughtItemById(int iid){
		String sql = "select item.iid,sid,iname,abs,introduce,sold,boughtlist.bprice as price,picture "
				+ " from item,boughtlist where item.iid =:iid and boughtlist.iid=item.iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	
	//########################
	//购买商品
	/**
	 * 1.添加商品到购物车
	 */
	public void insertItemToCart(int iid,int bid,int count){
		paramMap.clear();
		paramMap.put("iid", iid);
		paramMap.put("bid", bid);
		paramMap.put("count", count);
		
		String sql1 = "select iid from cart where bid = :bid and iid=:iid";
		//首先判断物品是否在购物车中已有
		try{
			this.jdbcTemplate.queryForObject(sql1, paramMap, Integer.class);
		}
		catch(EmptyResultDataAccessException e){
			//购物车中没有此商品，此时做插入操作！
			String sql3 = "insert cart (iid,bid,count) values (:iid,:bid,:count)";
			jdbcTemplate.update(sql3,paramMap);
			return ;
		}
			//购物车中已含有此种商品,此时做更新操作！
			String sql2 ="update cart set count=count+:count where iid = :iid and bid = :bid";
			jdbcTemplate.update(sql2,paramMap);
	}
	/**
	 * 2.查看购物车商品
	 */
	public List<cartItem> selectItemsInCart(int bid){
		paramMap.clear();
		paramMap.put("bid",bid);
		
		String sql = "select bid,item.iid,count,iname,price,picture from item,cart "
				+ "where item.iid = cart.iid and cart.bid=:bid ";
		
		return this.jdbcTemplate.query(sql, paramMap, new cartItemRowMapper());		
		
	}
	/**
	 * 3.提交订单信息，执行一个同步事务
	 * @return 
	 */
	@Transactional
	public void insertItemToBoughtList(int bid){
		paramMap.clear();
		paramMap.put("bid", bid);
		//这条sql先找出对应用户购物车中的全部商品
		String sql1 = "select cart.bid as bid, cart.iid as iid,cart.count as num ,"
				+ " item.iname as biname,item.price as bprice,item.picture as picture,'' as time"
				+ " from cart , item where cart.iid = item.iid and cart.bid=:bid";
		
		 List<boughtlistItem> buyItems = this.jdbcTemplate.query(sql1 , paramMap,new boughtlistRowMapper() );
		//删除购物车中对应用户的内容
		 String sql2 = "delete from cart where bid = :bid";
		 
		 this.jdbcTemplate.update(sql2, paramMap);
		 //将购物车中内容添加如已买列表
		 for(boughtlistItem item:buyItems){
			 String sql3 = "insert boughtlist(bid,iid,biname,num,time,bprice) values "
		 		+ "(:bid,:iid,:biname,:num,:time,:bprice)";
			 String sql4 = "update item set sold = sold + :num where iid = :iid";
			 paramMap.clear();
			 paramMap.put("bid", item.getBid());
			 paramMap.put("iid", item.getIid());
			 paramMap.put("biname",item.getBiname());
			 paramMap.put("num", item.getNum());
			 paramMap.put("time", item.getTime());
			 paramMap.put("bprice", item.getBprice());
			 paramMap.put("picture", item.getPicture());
			 
			 
			 this.jdbcTemplate.update(sql3, paramMap);	
			 this.jdbcTemplate.update(sql4, paramMap);
		 }
	}
	 
	 /**
	  * 4.查看购买记录
	  */
	 public List<boughtlistItem> selectAllBoughtItems(int bid){
		 paramMap.clear();
		 paramMap.put("bid", bid);
		 
		 String sql = "select bl.blid,bl.bid,bl.iid, bl.biname,bl.num,bl.time,bl.bprice,"
		 		+ "it.picture as picture from boughtlist bl,item it  where bid = :bid and it.iid=bl.iid "
		 		+ "order by blid desc";
		 
		 return this.jdbcTemplate.query(sql, paramMap, new boughtlistRowMapper());
	 }
	/**
	 * 5.分类查看未购买的商品列表
	 */
	 public List<item> selectAllItemsNotBought(int bid){
		 paramMap.clear();
		 paramMap.put("bid", bid);
		 
		 String sql = "select * from item where iid not in  (select iid from boughtlist where bid = :bid)";
		 
		 return this.jdbcTemplate.query(sql, paramMap, new itemRowMapper());
	 }
	 /**
	  * 6.分类查看已购买商品列表
	  */
	 public List<item> selectAllItemsBought(int bid){
		 paramMap.clear();
		 paramMap.put("bid", bid);
		 
		 String sql = "select * from item where iid in  (select iid from boughtlist where bid = :bid)";
		 
		 return this.jdbcTemplate.query(sql, paramMap, new itemRowMapper());
	 }
	 
	 /**
	  * 7.删除一条购物车信息
	  */
	public void deleteAnItemFromCart(int iid){
		paramMap.clear();
		paramMap.put("iid", iid);
		
		String sql = "delete from cart where iid=:iid";
		jdbcTemplate.update(sql,paramMap);
	}
	
}
