package com.centurywar.control;

//用户名注册
import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.UsersModel;

public class UserInfo extends BaseControl {

	public UserInfo() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String username = jsonObj.getString("username");
		String sec = jsonObj.getString("sec");
		UsersModel am = new UsersModel(username, sec);

		jsonObj.put("info", UsersModel.getInfo(am.gameuid));
		jsonObj.put("device", UsersModel.getAllUserDevice(am.gameuid));
		int ArduinoLoginTime = UsersModel.getLastArduinoLogin(am.gameuid);
		jsonObj.put("last_arduino_login", getTime() - ArduinoLoginTime);
		sendToSocket(jsonObj, ConstantControl.GET_USER_INFO);
	}
}
