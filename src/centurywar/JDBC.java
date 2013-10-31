package centurywar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC {
	// 创建静态全局变量
	static Connection conn;
	static Statement st;
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();
	/* 查询数据库，输出符合要求的记录的情况 */

	public static ResultSet query(String sql) {
		conn = getConnection("127.0.0.1:3306/goe", "root", ""); // 同样先要获取连接，即连接到数据库
		ResultSet rs = null;
		try {
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
		} catch (SQLException e) {
			System.out.println("[Error]" + sql);
		}
		return rs;
	}

	/* 获取数据库连接的函数 */
	public static Connection getConnection(String host, String username,
			String password) {
		if (connectpoll.containsKey(host)) {
			return connectpoll.get(host);
		}
		Connection con = null; // 创建用于连接数据库的Connection对象
		try {
			Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动
			con = DriverManager.getConnection("jdbc:mysql://" + host, username,
					password);// 创建数据连接
			connectpoll.put(host, con);
		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con; // 返回所建立的数据库连接
	}
}