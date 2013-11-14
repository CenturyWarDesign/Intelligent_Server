package com.centurywar.control;


import net.sf.json.JSONObject;

import com.centurywar.Main;
import com.centurywar.User;

/**
 * @author Administrator 接收板子的请求字符串，进行数据分发组装。
 *         message格式：传感器类型_引脚_值_附加位（若值是温度20.5，则值为20，附加位为5）
 */
/**
 * @author Administrator
 * 
 */
public class MessageControl {
	public static String MessageControl(String message, int gameuid,
			int fromgameuid) {
		// 如果是板子返回的信息
		if (message.substring(0, 1).equals("r")) {
			return controlReturn(message);
		}
		// 如果是板子发来的信息，进行处理
		if (Integer.parseInt(message.substring(0, 2)) > 0) {
			String[] temp = null;
			temp = message.trim().split("_");
			// 4个是标准输入
			if (temp[0].equals("r")) {
				return "";
			}
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWrite(fromgameuid, gameuid, "error message", false);
				return "";
			}
			if (temp[0].equals("30")) {
				double temValue = 0.0;
				temValue = Float.parseFloat(temp[2] + "." + temp[3]) / 100;
				// 温度计返回值
				if (gameuid == 0) {
					gameuid = fromgameuid;
				}
				User u = new User(gameuid);
				u.setFromgameuid(fromgameuid);
				u.updateTemperature(temValue, 1);
			}
		}

		JSONObject getJson = null;
		try {
			getJson = JSONObject.fromObject(message);
			controlBetch(getJson);
		} catch (Exception e) {
			System.out.println("Json Formate Error:" + message);
			return "Error Send" + message;
		}
		return "";
	}

	/**
	 * 处理安卓发来的信息,进行分发
	 * 
	 * @param command
	 * @param control
	 * @param gameuid
	 * @param fromgameuid
	 */
	public static void controlBetch(JSONObject jsonObj) {
		if (jsonObj.getString("control").equals("cup")) {
			CheckPassword.betch(jsonObj);
		} else if (jsonObj.getString("control").equals("sst")) {
			SetStatus.betch(jsonObj);
		}
	}

	/**
	 * 处理板子返回的信息
	 * 
	 * @param command
	 * @return
	 */
	public static String controlReturn(String command) {
		return "";
	}
}
