package com.centurywar.control;

import java.io.IOException;
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
		System.out.println("进入登录验证逻辑");
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
			String sec = EncoderPwdByMd5(String.format("Intelligent%d%d",temp1, uid));
			int isSuccess = JDBC.update(String
					.format("update users set sec='%s' where id='%d'",
							sec, uid));
			if(isSuccess<1){
				System.out.println("将新生成的验证码写入数据库失败，登录失败");
				return;
			}
			jsonObj.put("sec", sec);
//			Main.socketWrite(jsonObj.getInt("gameuid"), jsonObj.getInt("fromgameuid"), jsonObj.toString(), false);
			sendToSocket(jsonObj, ConstantControl.ECHO_CHECK_USERNAME_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int checkPassword(String username, String password) {
		JSONObject obj = JDBC
				.selectOne(String
						.format("select id from users where username='%s' and password='%s' limit 1",
								username, password));
		if (!obj.isEmpty()) {
			return obj.getInt("id");
		}
		return 0;
	}
}
