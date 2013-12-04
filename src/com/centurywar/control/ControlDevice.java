package com.centurywar.control;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.Redis;

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
			sendToSocketDevice(control, gameuid, false);
			System.out.println(String.format("[send to device %d]%s", gameuid,
					control));
			
			// 存入缓存
			String key = gameuid+":"+control;
			Integer time = new Integer((int) (System.currentTimeMillis()/1000));
			Redis.hset("cachedCommands",key,time.toString());
			System.out.println("缓存至：" + key + ",等待板子反馈");
		}
	}

}
