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
	//             ��ʾ��Ʒ
	/**1
	 * δ��¼״̬Ĭ����ʾȫ�����ҵ�ȫ����Ʒ
	 */
	public List<item> selectAllItems(){
		String sql = "select * from item";
		return this.jdbcTemplate.query(sql, new itemRowMapper());
	}
	/**2
	 * �鿴�����£���Ҳ鿴һ��δ������Ʒ
	 */
	public item selectItemById(int iid){
		String sql = "select * from item where iid = :iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	/**3
	 *  �鿴�����£���Ҳ鿴һ���ѹ�����Ʒ
	 *  Ψһ��֮ͬ�����ڣ��ѹ�����Ʒ�ļ۸���Ҫȥboughtlist�ѹ����б��л�ȡ��
	 */
	public item selectBoughtItemById(int iid){
		String sql = "select item.iid,sid,iname,abs,introduce,sold,boughtlist.bprice as price,picture "
				+ " from item,boughtlist where item.iid =:iid and boughtlist.iid=item.iid";
		paramMap.clear();
		paramMap.put("iid", iid);
		return this.jdbcTemplate.queryForObject(sql, paramMap,new itemRowMapper());
	}
	
	//########################
	//������Ʒ
	/**
	 * 1.�����Ʒ�����ﳵ
	 */
	public void insertItemToCart(int iid,int bid,int count){
		paramMap.clear();
		paramMap.put("iid", iid);
		paramMap.put("bid", bid);
		paramMap.put("count", count);
		
		String sql1 = "select iid from cart where bid = :bid and iid=:iid";
		//�����ж���Ʒ�Ƿ��ڹ��ﳵ������
		try{
			this.jdbcTemplate.queryForObject(sql1, paramMap, Integer.class);
		}
		catch(EmptyResultDataAccessException e){
			//���ﳵ��û�д���Ʒ����ʱ�����������
			String sql3 = "insert cart (iid,bid,count) values (:iid,:bid,:count)";
			jdbcTemplate.update(sql3,paramMap);
			return ;
		}
			//���ﳵ���Ѻ��д�����Ʒ,��ʱ�����²�����
			String sql2 ="update cart set count=count+:count where iid = :iid and bid = :bid";
			jdbcTemplate.update(sql2,paramMap);
	}
	/**
	 * 2.�鿴���ﳵ��Ʒ
	 */
	public List<cartItem> selectItemsInCart(int bid){
		paramMap.clear();
		paramMap.put("bid",bid);
		
		String sql = "select bid,item.iid,count,iname,price,picture from item,cart "
				+ "where item.iid = cart.iid and cart.bid=:bid ";
		
		return this.jdbcTemplate.query(sql, paramMap, new cartItemRowMapper());		
		
	}
	/**
	 * 3.�ύ������Ϣ��ִ��һ��ͬ������
	 * @return 
	 */
	@Transactional
	public void insertItemToBoughtList(int bid){
		paramMap.clear();
		paramMap.put("bid", bid);
		//����sql���ҳ���Ӧ�û����ﳵ�е�ȫ����Ʒ
		String sql1 = "select cart.bid as bid, cart.iid as iid,cart.count as num ,"
				+ " item.iname as biname,item.price as bprice,item.picture as picture,'' as time"
				+ " from cart , item where cart.iid = item.iid and cart.bid=:bid";
		
		 List<boughtlistItem> buyItems = this.jdbcTemplate.query(sql1 , paramMap,new boughtlistRowMapper() );
		//ɾ�����ﳵ�ж�Ӧ�û�������
		 String sql2 = "delete from cart where bid = :bid";
		 
		 this.jdbcTemplate.update(sql2, paramMap);
		 //�����ﳵ����������������б�
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
	  * 4.�鿴�����¼
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
	 * 5.����鿴δ�������Ʒ�б�
	 */
	 public List<item> selectAllItemsNotBought(int bid){
		 paramMap.clear();
		 paramMap.put("bid", bid);
		 
		 String sql = "select * from item where iid not in  (select iid from boughtlist where bid = :bid)";
		 
		 return this.jdbcTemplate.query(sql, paramMap, new itemRowMapper());
	 }
	 /**
	  * 6.����鿴�ѹ�����Ʒ�б�
	  */
	 public List<item> selectAllItemsBought(int bid){
		 paramMap.clear();
		 paramMap.put("bid", bid);
		 
		 String sql = "select * from item where iid in  (select iid from boughtlist where bid = :bid)";
		 
		 return this.jdbcTemplate.query(sql, paramMap, new itemRowMapper());
	 }
	 
	 /**
	  * 7.ɾ��һ�����ﳵ��Ϣ
	  */
	public void deleteAnItemFromCart(int iid){
		paramMap.clear();
		paramMap.put("iid", iid);
		
		String sql = "delete from cart where iid=:iid";
		jdbcTemplate.update(sql,paramMap);
	}
	
}
