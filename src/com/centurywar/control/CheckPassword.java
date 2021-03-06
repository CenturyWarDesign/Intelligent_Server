package com.centurywar.control;

import java.io.IOException;
import java.util.Random;

import net.sf.json.JSONObject;

import com.centurywar.JDBC;
import com.centurywar.Main;
import com.centurywar.UsersModel;

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
		System.out.println("用户的ID为：" + uid);
		if (uid == 0) {
			System.out.println("登陆失败");
			UsersModel.sendErrorTem(ConstantCode.USER_OR_PASSWORD_ERROR,
					gameuid);
			return;
		} else {
			Random r = new Random();
			int temp1 = r.nextInt();
			String sec = EncoderPwdByMd5(String.format("Intelligent%d%d",
					temp1, uid));
			int isSuccess = JDBC.update(String.format(
					"update users set sec='%s' where id='%d'", sec, uid));
			if (isSuccess < 1) {
				UsersModel.sendError(ConstantCode.USER_OR_PASSWORD_ERROR, uid);
			} else {
				jsonObj.put("info", UsersModel.getInfo(uid));
				jsonObj.put("device", UsersModel.getAllUserDevice(uid));
				jsonObj.put("last_arduino_login",
						getTime() - UsersModel.getLastArduinoLogin(uid));
				jsonObj.put("sec", sec);
				jsonObj.put("retCode", "0000");
				jsonObj.put("memo", "登陆成功");
				jsonObj.remove("tem");
				jsonObj.put("gameuid", uid);
				Main.moveSocketInGlobal(gameuid + "", uid);
				System.out.println("登陆验证完成：" + jsonObj);
			}

		}
		sendToSocket(jsonObj, ConstantControl.ECHO_CHECK_USERNAME_PASSWORD);
	}

	public static int checkPassword(String username, String password) {
		JSONObject obj = null;
		obj = JDBC
				.selectOne(String
						.format("select id from users where username='%s' and password='%s' limit 1",
								username, password));
		System.out.println("登陆验证，查询数据库的结果：" + obj);

		if (!obj.isEmpty()) {
			String sql = String.format(
					"insert into applogin (time,account) values (%d,%d)",
					getTime(), obj.getInt("id"));
			JDBC.query(sql);
			return obj.getInt("id");
		}
		return 0;
	}

}
