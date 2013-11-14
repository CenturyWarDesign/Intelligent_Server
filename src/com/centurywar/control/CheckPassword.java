package com.centurywar.control;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;

import com.centurywar.JDBC;
import com.centurywar.Main;

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
		if(uid ==0){
			System.out.println("登陆失败");
			Main.socketWrite(6, gameuid, "Login fail", false);
			return;
		}
		Random r=new Random();
		int temp1=r.nextInt();
		try{
			String sec =EncoderPwdByMd5(String.format("Intelligent%d%d", temp1,uid));
			System.out.println(sec);
			Main.socketWrite(6, gameuid, sec, false);
		}catch(Exception e){
			e.printStackTrace();
		}
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
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return 0;
	}
}
