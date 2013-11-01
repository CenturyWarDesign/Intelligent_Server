package com.centurywar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC {
	static Connection conn;
	static Statement st;
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();

	public static ResultSet query(String sql) {
		conn = getConnection("127.0.0.1:3306/intelligent", "root", "");
		ResultSet rs = null;
		try {
			st = (Statement) conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("[Error]" + sql);
		}
		return rs;
	}

	public static Connection getConnection(String host, String username,
			String password) {
		if (connectpoll.containsKey(host)) {
			return connectpoll.get(host);
		}
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + host, username,
					password);
			connectpoll.put(host, con);
		} catch (Exception e) {
			System.out.println("连接失败" + e.getMessage());
		}
		return con;
	}
}