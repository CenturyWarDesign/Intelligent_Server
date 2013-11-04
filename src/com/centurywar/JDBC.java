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
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();

	public static ResultSet select(String sql) {
		conn = getConnection("127.0.0.1:3306/intelligent", "root", "");
		ResultSet rs = null;
		try {
			Statement st;
			st = (Statement) conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("[Error]" + sql + e.toString());
		}
		return rs;
	}

	public static boolean query(String sql) {
		conn = getConnection("127.0.0.1:3306/intelligent", "root", "");
		try {
			Statement st = (Statement) conn.createStatement();
			return st.execute(sql);
		} catch (SQLException e) {
			System.out.println("[Error]" + sql + e.toString());
		}
		return false;
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