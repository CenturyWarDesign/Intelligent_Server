package com.centurywar.control;

//用户名注册
import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.UsersModel;

public class UserReg extends BaseControl {

	public UserReg() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		String username = jsonObj.getString("username");
		String password = jsonObj.getString("password");
		int gameuid = jsonObj.getInt("gameuid");
		boolean userIsInit = UsersModel.UserReg(username, password);
		if (!userIsInit) {
			UsersModel.sendErrorTem(ConstantCode.USER_OR_PASSWORD_CANT_USE,
					gameuid);
		} else {
			UsersModel.sendErrorTem(ConstantCode.USER_REG_SUCCESS,
					gameuid);
		}
	}
}
