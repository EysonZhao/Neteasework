package com.netease.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.netease.beans.cartItem;

public class cartItemRowMapper implements RowMapper<cartItem> {

	public cartItem mapRow(ResultSet rs, int arg1) throws SQLException {
		if(rs==null){
			return null;
		}
		cartItem it = new cartItem(
				rs.getInt("bid"), 
				rs.getInt("iid"), 
				rs.getInt("count"), 
				rs.getString("iname"),
				rs.getDouble("price"),
				rs.getString("picture")
				);
		return it;
	}

}
