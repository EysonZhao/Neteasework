package com.netease.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.netease.RowMapper.itemRowMapper;
import com.netease.beans.item;
@Repository
public class SellerBehaviorDao {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private Map<String,Object> paramMap = new  HashMap<String,Object>();
	
	@Autowired
	public void setDataSource(BasicDataSource datasource){
		jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}
	//############
	//     查看商品
	public item selectItemById(int iid){
		String sql = "select * from item where iid = :iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	
	//############
	//     操作商品
	/**
	 * 编辑界面下，卖家增加一个商品
	 */
	public void insertAnItemBySeller(int sid,String iname,String abs,String introduce,double price,String picture){
		paramMap.clear();
		paramMap.put("sid", sid);
		paramMap.put("iname", iname);
		paramMap.put("abs", abs);
		paramMap.put("introduce", introduce);
		paramMap.put("price", price);
		paramMap.put("picture", picture);
		
		String sql = "insert item (sid,iname,abs,introduce,price,picture) "
				+ " values (:sid,:iname,:abs,:introduce,:price,:picture) " ;

		this.jdbcTemplate.update(sql, paramMap);
		
	}
	/**
	 * 功能--编辑界面下，卖家更改一个商品
	 * 前端-- /modifyitem的post方法
	 */
	public void updateItemBySeller(int iid,String iname,String abs, String introduce,double price,String picture){
		paramMap.clear();
		paramMap.put("iid", iid);
		paramMap.put("iname", iname);
		paramMap.put("abs", abs);
		paramMap.put("introduce", introduce);
		paramMap.put("price", price);
		paramMap.put("picture", picture);
		
		String sql = "update item set iname=:iname, abs=:abs, introduce=:introduce, price=:price, picture=:picture "
				+ " where iid=:iid" ;

		this.jdbcTemplate.update(sql, paramMap);
	}
	/**
	 *  查看界面下，卖家删除一个未卖出的商品
	 *  对应 /delete?id=xxx
	 */
	public void deleteItemBySeller(int iid){
		paramMap.clear();
		paramMap.put("iid", iid);
		String sql = "delete from item where iid =:iid ";
		this.jdbcTemplate.update(sql, paramMap);
	}
	/**
	 * 功能--查看界面下，卖家所有已卖出的商品
	 * 前端--在 /items主界面中填充otherlist
	 */
	public List<item> selectAllSoldItems(int sid){
		paramMap.clear();
		paramMap.put("sid", sid);
		
		String sql = "select * from item where sold>0 and sid =:sid";
		return this.jdbcTemplate.query(sql, paramMap,new itemRowMapper());
	}
	/**
	 * 功能--查看卖家所有尚未卖出的商品
	 * 前端--/items主界面中填充itemlist
	 */
	public List<item> selectAllUnsoldItems(int sid){
		paramMap.clear();
		paramMap.put("sid", sid);
		
		String sql = "select * from item where sold=0 and sid =:sid";
		return this.jdbcTemplate.query(sql, paramMap,new itemRowMapper());
	}
	
	/**
	 * ！！！！only for test!！！！
	 * 清空买家买的全部记录，恢复数据库！
	 */
	public void resetDB(){
		String sql = "delete from boughtlist; update item set sold=0";
		this.jdbcTemplate.update(sql, paramMap);
	}
			
	
	
	
}
