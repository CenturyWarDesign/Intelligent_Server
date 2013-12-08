package com.centurywar.control;

//更新用户传感器信息的地方
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.centurywar.UsersModel;

public class UpdateUserDevice extends BaseControl {

	public UpdateUserDevice() throws IOException {
		super();

		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		UsersModel am = getUserModel(jsonObj);
		am.UpdateUserDevice(jsonObj.getJSONArray("device"));
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
