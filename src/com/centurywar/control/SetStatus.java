package com.centurywar.control;

//设置延迟时间
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Random;

import net.sf.json.JSONObject;

import com.centurywar.Behave;
import com.centurywar.JDBC;
import com.centurywar.Main;

public class SetStatus extends BaseControl {

	public SetStatus() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {

		int type = jsonObj.getInt("type");
		int pik = jsonObj.getInt("pik");
		int command = jsonObj.getInt("command");
		int value = jsonObj.getInt("value");
		// 有延时的开关
		if (type == 10 && value > 0) {
			Behave be = new Behave(0);
			be.newInfo(gameuid, gameuid, -value,
					getBehaver(type, pik, command, value));
		}
	}

}
