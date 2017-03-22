package com.netease.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private Map<String,Object> paramMap = new  HashMap<String,Object>();
	
	public void initialize(String name,String password){
		paramMap.put("name", name);
		paramMap.put("password", password);
	}
	
	@Autowired
	public void setDataSource(BasicDataSource datasource){
		jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}
	
	public int loginSeller(){
		
		String sql = "select sid from seller where sname = :name and spassword = :password";
		int result = 0;
		try {
			result = this.jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		} catch (Exception e) {
			return result;
		}
		return result;
	}
	
	public int loginBuyer(){
		
		String sql = "select bid from buyer where bname = :name and bpassword = :password";
		int result = 0;
		try {
			result = this.jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		} catch (Exception e) {
			return result;
		}
		return result;
	}
}
