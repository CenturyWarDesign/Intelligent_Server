package com.centurywar.control;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.centurywar.User;

public class GetUserTemperaTure extends BaseControl {

	public GetUserTemperaTure() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		User us = new User(0);
		JSONArray re= null;
		try {
			re = us.getUserDevice(5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendToSocket(re, ConstantControl.ECHO_GET_USER_TEMPERATURE);
	}

}
