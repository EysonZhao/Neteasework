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
	//     �鿴��Ʒ
	public item selectItemById(int iid){
		String sql = "select * from item where iid = :iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	
	//############
	//     ������Ʒ
	/**
	 * �༭�����£���������һ����Ʒ
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
	 * ����--�༭�����£����Ҹ���һ����Ʒ
	 * ǰ��-- /modifyitem��post����
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
	 *  �鿴�����£�����ɾ��һ��δ��������Ʒ
	 *  ��Ӧ /delete?id=xxx
	 */
	public void deleteItemBySeller(int iid){
		paramMap.clear();
		paramMap.put("iid", iid);
		String sql = "delete from item where iid =:iid ";
		this.jdbcTemplate.update(sql, paramMap);
	}
	/**
	 * ����--�鿴�����£�������������������Ʒ
	 * ǰ��--�� /items�����������otherlist
	 */
	public List<item> selectAllSoldItems(int sid){
		paramMap.clear();
		paramMap.put("sid", sid);
		
		String sql = "select * from item where sold>0 and sid =:sid";
		return this.jdbcTemplate.query(sql, paramMap,new itemRowMapper());
	}
	/**
	 * ����--�鿴����������δ��������Ʒ
	 * ǰ��--/items�����������itemlist
	 */
	public List<item> selectAllUnsoldItems(int sid){
		paramMap.clear();
		paramMap.put("sid", sid);
		
		String sql = "select * from item where sold=0 and sid =:sid";
		return this.jdbcTemplate.query(sql, paramMap,new itemRowMapper());
	}
	
	/**
	 * ��������only for test!������
	 * ���������ȫ����¼���ָ����ݿ⣡
	 */
	public void resetDB(){
		String sql = "delete from boughtlist; update item set sold=0";
		this.jdbcTemplate.update(sql, paramMap);
	}
			
	
	
	
}
