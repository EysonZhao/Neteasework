package com.netease.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.netease.beans.item;

public class itemRowMapper implements RowMapper<item> {

	public item mapRow(ResultSet rs, int rows) throws SQLException {
		item it = new item(rs.getInt("iid"), 
				rs.getInt("sid"), 
				rs.getString("iname"),
				rs.getString("abs"),
				rs.getString("introduce"),
				rs.getInt("sold"),
				rs.getDouble("price"),
				rs.getString("picture"));
		return it;
	}

}
