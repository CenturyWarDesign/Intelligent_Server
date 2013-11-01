package com.centurywar;

import java.sql.ResultSet;

public class User {
	public int gameuid = 0;
	private String secGameuid = "";
	public String userName = "";
	public String ip = "0.0.0.0";
	public int port = 80;
	public User(String sec) {
		secGameuid = sec;
		getUserInfo();
	}

	private User getUserInfo() {
		try {
			ResultSet rs = JDBC.query(String.format(
					"select * from users where password='%s'", secGameuid));
			while (rs.next() && rs != null) {
				userName = rs.getString("username");
				gameuid = rs.getInt("id");
				return this;
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return this;

	}
}
