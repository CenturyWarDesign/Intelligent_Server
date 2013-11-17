package com.centurywar.control;

import java.io.IOException;

import net.sf.json.JSONObject;

//控制板子指令
public class ControlDevice extends BaseControl {

	public ControlDevice() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String control = getSendStringFromJsonObject(jsonObj);
		if (control.length() > 0) {
			int gameuid = jsonObj.getInt("gameuid");
			sendToSocketDevice(control, gameuid);
			System.out.println(String.format("[send to device %d]%s", gameuid,
					control));
		}
	}

}
