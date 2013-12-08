package com.centurywar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBC {
	protected final static Log Log = LogFactory.getLog(JDBC.class);
	static Connection conn;
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();

	public static void initConn() throws Exception {
//		conn = getConnection("127.0.0.1:3306/intelligent", "root", "");
		 conn = getConnection("42.121.123.185:3306/intelligent", "root",
				"abc123");
	}

	/**
	 * 返回记录集，最核心查询语句
	 * 
	 * @param sql
	 * @return
	 */
	public static JSONArray select(String sql) {
		try {
			initConn();
			JSONArray jsa = new JSONArray();
			ResultSet rs = null;
			Statement st;
			st = (Statement) conn.createStatement();
			rs = st.executeQuery(sql);
			System.out.println("[mysql]" + sql);
			jsa = resultSetToJson(rs);
			return jsa;
		} catch (Exception e) {
			return new JSONArray();
		}
	}

	/**
	 * 返回一条记录
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static JSONObject selectOne(String sql) {
		JSONArray tem = select(sql);
		if (tem.size() == 0) {
			return new JSONObject();
		}
		return (JSONObject) select(sql).get(0);
	}
	
	/**
	 * 运行一条SQL update/delete
	 * 
	 * @param sql
	 * @return
	 */
	public static int update(String sql) {
		try {
			initConn();
			Statement st = (Statement) conn.createStatement();
			return st.executeUpdate(sql);
		} catch (Exception e) {
			Log.error(e.toString());
			return -1;
		}
	}

	/**
	 * 运行一条SQL insert/delete
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean query(String sql) {
		try {
			initConn();
			Statement st = (Statement) conn.createStatement();
			String[] sqlarr = sql.split(";");
			for (int i = 0; i < sqlarr.length; i++) {
				st.execute(sqlarr[i]);
				System.out.println("[query sql]" + sqlarr[i]);
			}
			return true;
		} catch (Exception e) {
			Log.error(e.toString());
			return false;
		}
	}

	public static Connection getConnection(String host, String username,
			String password) throws Exception {
		if (connectpoll.containsKey(host)) {
			return connectpoll.get(host);
		}
		Connection con = null;
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://" + host, username,
				password);
		connectpoll.put(host, con);
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

	/**
	 * 从jsonsobject得到insert数据
	 * 
	 * @param obj
	 * @param tablename
	 * @return
	 */
	public static String insertStringFromJSONObject(JSONObject obj,
			String tablename) {
		Iterator it = obj.keys();
		String keystr = "";
		String valuestr = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = obj.getString(key);
			keystr += "`" + key + "`,";
			valuestr += "'" + value + "',";
		}
		return String.format("insert into %s (%s) values (%s);", tablename,
				keystr.substring(0, keystr.length() - 1),
				valuestr.substring(0, valuestr.length() - 1));
	}
}