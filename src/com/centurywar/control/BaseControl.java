package com.centurywar.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

import com.centurywar.Main;
import com.centurywar.UsersModel;

public class BaseControl {
	protected static int gameuid = 0;
	
	
	
	public BaseControl() throws IOException {

	}

	public static boolean sendToSocket(JSONObject jsonObj, String command) {
		// 不需要判断是否存在，
		if (!jsonObj.containsKey("sendTime")) {
			jsonObj.put("sendTime", getTime());
		}
		jsonObj.put("control", command);
		System.out.println("写向android的报文为：" + jsonObj.toString());
		// 判断返回池类别，如果是临时的，写入临时表

		int fromgameuid = jsonObj.containsKey("fromgameuid") ? jsonObj
				.getInt("fromgameuid") : 0;

		if (command.equals(ConstantControl.SET_STATUS)) {
			Main.socketWriteAll(jsonObj.getInt("gameuid"),
					jsonObj.getInt("gameuid"),
					jsonObj.getString("sendToArduino"), true,
					ConstantControl.WRITE_ARDUINO_HANDLER);
			return true;
		} else {
			if (jsonObj.containsValue("tem")) {
				Main.socketWriteAll(jsonObj.getInt("gameuid"),
						jsonObj.getInt("gameuid"), jsonObj.toString(), false,
						ConstantControl.WRITE_TEM_HANDLER);
			} else {
				Main.socketWriteAll(jsonObj.getInt("gameuid"), fromgameuid,
						jsonObj.toString(), false,
						ConstantControl.WRITE_GLOBAL_HANDLER);
			}
		}
		return false;
	}

	public static boolean sendToSocketDevice(String command, int gameuid,
			boolean resend) {
		Main.socketWriteAll(gameuid, gameuid, command, resend,
				ConstantControl.WRITE_ARDUINO_HANDLER);
		return false;
	}

	public static boolean sendToSocket(JSONArray jsonArray, String command) {
		JSONObject obj = new JSONObject();
		obj.put("data", jsonArray);
		obj.put("gameuid", 6);
		return sendToSocket(obj, command);
	}

	/**
	 * MD5加密算法
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String EncoderPwdByMd5(String str) {
		try {
			// 确定计算方法
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 取得当前的时间
	 * 
	 * @return
	 */
	public static int getTime() {
		Date date = new Date();
		return (int) (date.getTime() / 1000);
	}

	public static String getBehaver(int type, int pik, int commmand, int value) {
		return String.format("%d_%d_%d_%d", type, pik, commmand, value);
	}
	
	
	/**
	 * 把JSONObject 转化为指令
	 * 
	 * @param obj
	 * @return
	 */
	protected static String getSendStringFromJsonObject(JSONObject obj) {
		if (!obj.has("type") || !obj.has("pik") || !obj.has("value")
				|| !obj.has("data")) {
			return "";
		} else {
			try {
				return String.format("%d_%d_%d_%d", obj.getInt("type"),
						obj.getInt("pik"), obj.getInt("value"),
						obj.getInt("data"));
			} catch (Exception e) {
				System.out.print(e.toString());
				return "";
			}
		}

	}

	protected static UsersModel getUserModel(JSONObject jsonObj) {
		String sec = jsonObj.getString("sec");
		String username = jsonObj.getString("username");
		UsersModel am = new UsersModel(username, sec);
		return am;
	}
}
