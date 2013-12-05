package com.centurywar.control;

import net.sf.json.JSONObject;

/**
 * @author Administrator 接收板子的请求字符串，进行数据分发组装。
 *         message格式：传感器类型_引脚_值_附加位（若值是温度20.5，则值为20，附加位为5）
 */
/**
 * @author Administrator
 * 
 */
public class MessageControl {
	public static String MessageControl(String message, int id, int fromid,
			boolean tem) {
		System.out.println("gameuid:" + id);
		// 如果是板子返回的信息
		if (message.length() == 0) {
			return "";
		}
		// 服务器反馈处理，从缓存中删除
		if (message.substring(0, 1).equals("r")) {
			ArduinoControl.controlReturn(id, message);
			return "";
		}
		System.out.println(message.contains("{"));
		if (message.contains("{")) {
			System.out.println("1111");
			JSONObject getJson = null;
			try {
				getJson = JSONObject.fromObject(message);
				getJson.put("gameuid", id);
				getJson.put("fromgameuid", fromid);
				getJson.put("tem", tem);
				controlBetch(getJson);
			} catch (Exception e) {
				System.out.println(e.toString());
				return "";
			}
		} else {
			ArduinoControl.controlArduinoSend(message, id, fromid);
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
		} else if (jsonObj.getString("control").equals(
				ConstantControl.SET_STATUS)) {
			SetStatus.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
				ConstantControl.GET_USER_TEMPERATURE)) {
			GetUserTemperaTure.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
				ConstantControl.CONTROL_DEVICE)) {
			ControlDevice.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
				ConstantControl.UPDAT_DEVICE_TO_SERVER)) {
			UpdateUserDevice.betch(jsonObj);
		}
	}

	

	// /**
	// * 处理板子返回的信息
	// *
	// * @param message
	// * @return
	// */
	// public static long controlReturn(String message,int gameuid) {
	// String originCommand = message.substring(2);
	// System.out.print("得到反馈，删除缓存：" + gameuid + originCommand);
	// String key = gameuid+":"+message;
	// // 把反馈回传到客户端
	// JSONObject obj = new JSONObject();
	// obj.put("command", message);
	// obj.put("gameuid", gameuid);
	// EchoSetStatus.betch(obj);
	// return Redis.hdel("cachedCommands",key);
	// }
	
	
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("username", "wanbin");
		json.put("control", "cpd");
		json.put("password", "7a941492a0dc743544ebc71c89370a64");
		System.out.println(json.toString().contains("{"));
	}
}
