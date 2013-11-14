package com.centurywar.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;

import com.centurywar.Main;

import net.sf.json.JSONObject;

public class BaseControl {
	protected static int gameuid = 0;

	public BaseControl() throws IOException {

	}

	public static void main(String[] args) throws IOException {
//		Map<String, Boolean> ate = new HashMap<String, Boolean>();
//		ate.put("name", false);
//		JSONObject.fromObject(ate).toString();
//		System.out.println(JSONObject.fromObject(ate).toString());
		
		JSONObject getJson = JSONObject.fromObject("{23:10_3_3_0}");
		System.out.println(getJson.toString());
	}

	public static boolean sendToSocket(JSONObject jsonArray, String command) {
		if (!jsonArray.containsKey("sendTime")) {
			jsonArray.put("sendTime", getTime());
		}
		Main.socketWrite(gameuid, gameuid, jsonArray.toString(), false);
		return false;
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
}
