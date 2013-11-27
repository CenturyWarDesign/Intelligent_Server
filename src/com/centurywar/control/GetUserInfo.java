package com.centurywar.control;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.centurywar.ArduinoModel;
import com.centurywar.UsersModel;

public class GetUserInfo extends BaseControl {

	public GetUserInfo() throws IOException {
		super();

		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String sec = jsonObj.getString("sec");
		String username = jsonObj.getString("username");
		UsersModel am = new UsersModel(username, sec);
		JSONArray re = null;
		try {
			re = am.getUserDevice(am.gameuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("data", re);
		obj.put("gameuid", am.gameuid);
		sendToSocket(obj, ConstantControl.ECHO_GET_USER_TEMPERATURE);
	}
}
