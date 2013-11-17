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
		System.out.println("gameuid"+gameuid);
		System.out.println("fromgameuid"+fromgameuid);
		// 如果是板子返回的信息
		if (message.length() == 0) {
			return "";
		}
		if (message.substring(0, 1).equals("r")) {
			return controlReturn(message);
		}
		System.out.println("[get from client]" + message);
		System.out.println(message.contains("{"));
		if (message.contains("{")) {
			System.out.println("1111");
			JSONObject getJson = null;
			try {
				getJson = JSONObject.fromObject(message);
				getJson.put("fromgameuid", fromgameuid);
				getJson.put("gameuid", gameuid);
				controlBetch(getJson);
			} catch (Exception e) {
				System.out.println(e.toString());
				return "";
			}
		} else {
			controlArduinoSend(message, gameuid, fromgameuid);
			return "";
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
		System.out.println("control:"+jsonObj.getString("control"));
		if (jsonObj.getString("control").equals(
				ConstantControl.CHECK_USERNAME_PASSWORD)) {
			CheckPassword.betch(jsonObj);
		} else if (jsonObj.getString("control").equals("sst")) {
			SetStatus.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
				ConstantControl.GET_USER_TEMPERATURE)) {
			GetUserTemperaTure.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
				ConstantControl.CONTROL_DEVICE)) {
			ControlDevice.betch(jsonObj);
		}
	}

	public static String controlArduinoSend(String message, int gameuid,
			int fromgameuid) {
		String[] temp = null;
		temp = message.trim().split("_");
		// 4个是标准输入
		if (temp[0].equals("r")) {
			return "";
		}
		if (temp[0].equals(ConstantControl.DEVICE_TEMPERATURE)) {
			double temValue = 0.0;
			try {
				temValue = Float.parseFloat(temp[2] + "." + temp[3]);
				// 温度计返回值
				if (gameuid == 0) {
					gameuid = fromgameuid;
				}
				User u = new User(gameuid);
				u.setFromgameuid(fromgameuid);
				u.updateTemperature(temValue, 1);
			} catch (Exception e) {

			}
		}
		// 这是火焰报警器
		if (temp[0].equals(ConstantControl.DEVICE_HUOJING)) {
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWrite(fromgameuid, gameuid, "error message", false);
				return "";
			}
			try {
				User u = new User(gameuid);
				// u.sendToPush(fromgameuid, "着火了", "家里有可能着火了");
			} catch (Exception e) {

			}
		}

		return "";
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
	
	
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("username", "wanbin");
		json.put("control", "cpd");
		json.put("password", "7a941492a0dc743544ebc71c89370a64");
		System.out.println(json.toString().contains("{"));
	}
}
