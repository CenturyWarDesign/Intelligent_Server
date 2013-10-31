package centurywar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBC {
	// ������̬ȫ�ֱ���
	static Connection conn;
	static Statement st;
	private static Map<String, Connection> connectpoll = new HashMap<String, Connection>();
	/* ��ѯ���ݿ⣬�������Ҫ��ļ�¼����� */

	public static ResultSet query(String sql) {
		conn = getConnection("127.0.0.1:3306/goe", "root", ""); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		ResultSet rs = null;
		try {
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����
			rs = st.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
		} catch (SQLException e) {
			System.out.println("[Error]" + sql);
		}
		return rs;
	}

	/* ��ȡ���ݿ����ӵĺ��� */
	public static Connection getConnection(String host, String username,
			String password) {
		if (connectpoll.containsKey(host)) {
			return connectpoll.get(host);
		}
		Connection con = null; // ���������������ݿ��Connection����
		try {
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			con = DriverManager.getConnection("jdbc:mysql://" + host, username,
					password);// ������������
			connectpoll.put(host, con);
		} catch (Exception e) {
			System.out.println("���ݿ�����ʧ��" + e.getMessage());
		}
		return con; // ���������������ݿ�����
	}
}