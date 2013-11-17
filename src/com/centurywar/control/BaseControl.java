package com.centurywar.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import sun.misc.BASE64Encoder;

import com.centurywar.Main;

public class BaseControl {
	protected static int gameuid = 0;
	
	// Redis,缓存数据库，对于一些频繁的查询要用
	protected static Jedis redis = new Jedis("127.0.0.1", 6379);
	
	public BaseControl() throws IOException {

	}

	public static boolean sendToSocket(JSONObject jsonObj, String command) {
		if (!jsonObj.containsKey("gameuid")) {
			return false;
		}
		int gameuid = jsonObj.getInt("gameuid");
		if (!jsonObj.containsKey("sendTime")) {
			jsonObj.put("sendTime", getTime());
		}
		jsonObj.put("control", command);
		Main.socketWrite(gameuid, gameuid, jsonObj.toString(), false);
		return false;
	}

	public static boolean sendToSocketDevice(String command, int gameuid) {
		Main.socketWrite(gameuid, gameuid, command, true);
		return false;
	}

	public static boolean sendToSocket(JSONArray jsonArray, String command) {
		JSONObject obj = new JSONObject();
		obj.put("data", jsonArray);
		obj.put("gameuid", 6);
		return sendToSocket(obj, command);
	}

	public static final String EncoderPwdByMd5(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		// 加密后的字符串
		String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
	public static int getTime() {
		Date date = new Date();
		return (int) (date.getTime() / 1000);
	}

	public static String getBehaver(int type, int pik, int commmand, int value) {
		return String.format("%d_%d_%d_%d", type, pik, commmand, value);
	}
	
	/**
	 * 设置字段至Redis
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String setToRedis(String key, String value) {
		return redis.set(key, value);
	}
	
	/**
	 * 从Redis读取
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getFromRedis(String key) {
		return redis.get(key);
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
}
