package com.centurywar.control;

//自动匹配客户端
import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.UsersModel;

public class AutoGetArduinoId extends BaseControl {

	public AutoGetArduinoId() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String username = jsonObj.getString("username");
		String sec = jsonObj.getString("sec");
		int gameuid = jsonObj.getInt("gameuid");
		UsersModel am = new UsersModel(username, sec);
		am.autoGetArduinoId();
	}
}
