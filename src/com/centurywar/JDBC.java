package com.centurywar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JDBC {
	static Connection conn;
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();

	public static void initConn() {
		conn = getConnection("127.0.0.1:3306/intelligent", "root", "");
	}

	/**
	 * 返回记录集
	 * 
	 * @param sql
	 * @return
	 */
	public static JSONArray select(String sql) {
		initConn();
		ResultSet rs = null;
		try {
			Statement st;
			st = (Statement) conn.createStatement();
			rs = st.executeQuery(sql);
			return resultSetToJson(rs);
		} catch (SQLException e) {
			System.out.println("[Error]" + sql + e.toString());
		}
		return null;
	}

	/**
	 * 返回一条记录
	 * 
	 * @param sql
	 * @return
	 */
	public static JSONObject selectOne(String sql) {
		return (JSONObject) select(sql).get(0);
	}

	/**
	 * 运行一条SQL update/delete
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean query(String sql) {
		initConn();
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

	/**
	 * 把resultset 转发化JSON
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static JSONArray resultSetToJson(ResultSet rs) throws SQLException,
			JSONException {
		// json数组
		JSONArray array = new JSONArray();
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();

			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.add(jsonObj);
		}
		return array;
	}
}