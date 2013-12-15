package com.centurywar.control;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.UsersModel;

//设置用户的模式
public class SetMode extends BaseControl {

	public SetMode() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String sec = jsonObj.getString("sec");
		String username = jsonObj.getString("username");
		int mode = jsonObj.getInt("mode");
		UsersModel am = new UsersModel(username, sec);
		am.setMode(mode);
		JSONObject obj = new JSONObject();
		obj.put("data", 1);
		obj.put("gameuid", am.gameuid);
		// 发送板子上线通知到客户端
		UsersModel.sendError(ConstantCode.USER_Mode_UPDATE_OK, am.gameuid);

	}

}
