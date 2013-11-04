package com.centurywar;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;

import sun.misc.BASE64Encoder;

public class CheckPassword {
	
	public static void betch(String control){
		String username;
		String password;
		int gameuid;
		String[] temp = null;
		temp = control.trim().split("_");
		username=temp[2];
		password=temp[3];
		gameuid= Integer.parseInt(temp[4]);
		int uid=checkPassword(username,password);
		Random r=new Random();
		int temp1=r.nextInt();
		String sec =EncoderPwdByMd5(String.format("Intelligent%d%d", temp1,uid));
		Main.socketWrite(gameuid, gameuid, sec, false);
	}
	public static final String EncoderPwdByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		// 加密后的字符串
		String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
}

	public static int checkPassword(String username, String password) {
		try {
			ResultSet rs = JDBC.select(String.format(
					"select id from users where username='%s' and password='%s' limit 1", username,password));
			while (rs.next() && rs != null) {
				return rs.getInt("id");
//				userName = rs.getString("username");
//				gameuid = rs.getInt("id");
//				client = rs.getInt("client_id");
//				return this;
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return 0;
	}
}
