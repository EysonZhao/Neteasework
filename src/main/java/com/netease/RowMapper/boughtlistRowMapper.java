package com.netease.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.netease.beans.boughtlistItem;

public class boughtlistRowMapper implements RowMapper<boughtlistItem> {

	public boughtlistItem mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String datetime ;
		if(rs.getString("time").equals("")){
			datetime= df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
		}
		else{
			datetime=rs.getString("time");
		}
		boughtlistItem it = new boughtlistItem(
				rs.getInt("bid"),
				rs.getInt("iid"),
				rs.getString("biname"),
				rs.getInt("num"),
				datetime,
				rs.getDouble("bprice"),
				rs.getString("picture"));
		return it;
	}

}
