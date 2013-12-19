package com.centurywar.control;

import net.sf.json.JSONObject;

import com.centurywar.Main;
import com.centurywar.UsersModel;

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
		
		System.out.println(message.contains("{"));
		if (message.contains("{")) {
			System.out.println("1111");
			JSONObject getJson = null;
			try {
				getJson = JSONObject.fromObject(message);
				getJson.put("gameuid", id);
				getJson.put("fromgameuid", fromid);
				getJson.put("tem", tem);
				// 如果是客户端重新连上的，根据sec直接转移到相应的里面
				if (tem && getJson.containsKey("sec")
						&& getJson.containsKey("username")
						&& !getJson.getString("control").equals(
						// 检查用户名及密码
								ConstantControl.CHECK_USERNAME_PASSWORD)) {
					String sec = getJson.getString("sec");
					String username = getJson.getString("username");
					UsersModel am = new UsersModel(username, sec);
					Main.moveSocketInGlobal(id + "", am.gameuid);
					// 发送板子上线通知到客户端
					UsersModel.sendError(ConstantCode.RE_CONNECT_ID_SUCCESS,
							am.gameuid);
				}
				controlBetch(getJson);
			} catch (Exception e) {
				e.printStackTrace();
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
		System.out.println("get from android:control:"
				+ jsonObj.getString("control"));
		if (jsonObj.getString("control").equals(
		// 检查用户名及密码
				ConstantControl.CHECK_USERNAME_PASSWORD)) {
			CheckPassword.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 设置传感器状态
				ConstantControl.SET_STATUS)) {
			SetStatus.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 取得用户的温度信息
				ConstantControl.GET_USER_TEMPERATURE)) {
			GetUserTemperaTure.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 传感器控制指令
				ConstantControl.CONTROL_DEVICE)) {
			ControlDevice.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 更新传感器引脚信息
				ConstantControl.UPDAT_DEVICE_TO_SERVER)) {
			UpdateUserDevice.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 更新用户模式
				ConstantControl.UPDAT_USER_MODE)) {
			SetMode.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 自动匹配板子
				ConstantControl.AUTO_GET_ARUDINO_ID)) {
			AutoGetArduinoId.betch(jsonObj);
		} else if (jsonObj.getString("control").equals(
		// 取得用户信息
				ConstantControl.GET_USER_INFO)) {
			UserInfo.betch(jsonObj);
		}
	}

	/**
	 * 测试代码，没什么用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("username", "wanbin");
		json.put("control", "cpd");
		json.put("password", "7a941492a0dc743544ebc71c89370a64");
		System.out.println(json.toString().contains("{"));
	}
}
