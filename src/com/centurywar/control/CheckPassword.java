package com.centurywar.control;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Random;
import net.sf.json.JSONObject;
import com.centurywar.JDBC;
import com.centurywar.Main;

public class CheckPassword extends BaseControl {

	public CheckPassword() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String username = jsonObj.getString("username");
		String password = jsonObj.getString("password");
		int gameuid = jsonObj.getInt("gameuid");
		int uid = checkPassword(username, password);
		if (uid == 0) {
			System.out.println("登陆失败");
			Main.socketWrite(6, gameuid, "Login fail", false);
			return;
		}
		Random r = new Random();
		int temp1 = r.nextInt();
		try {
			String sec = EncoderPwdByMd5(String.format("Intelligent%d%d",
					temp1, uid));
			System.out.println(sec);
			Main.socketWrite(6, gameuid, sec, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int checkPassword(String username, String password) {
		try {
			ResultSet rs = JDBC
					.select(String
							.format("select id from users where username='%s' and password='%s' limit 1",
									username, password));
			while (rs.next() && rs != null) {
				return rs.getInt("id");
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return 0;
	}
}
