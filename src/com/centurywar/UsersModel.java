package com.centurywar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UsersModel extends BaseModel {
	public final static double LIMIT = 100;
	public int gameuid = 0;

	private String secGameuid = "";
	public String userName = "";
	public String ip = "0.0.0.0";
	public int port = 80;
	public int client = 0;
	public ArduinoModel arduinoClient;
	
	public UsersModel(String sec) {
		secGameuid = sec;
		try {
			getUserInfoFromPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UsersModel(int gameuidsend) {
		if (gameuidsend > 0) {
			gameuid = gameuidsend;
			try {
				getUserInfoFromGameuid();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public UsersModel(String username, String password){
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where username='%s' and password='%s'",
				username, password));
		if (!obj.isEmpty()) {
			userName = obj.getString("username");
			gameuid = obj.getInt("id");
			client = obj.getInt("client_id");
		}
	}

	
	private UsersModel getUserInfoFromGameuid() throws Exception {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where id=%d", gameuid));
		if (!obj.isEmpty()) {
			userName = obj.getString("username");
			gameuid = obj.getInt("id");
			client = obj.getInt("client_id");
		}
		return this;
	}

	public int getGameuid() {
		return gameuid;
	}

	public void setGameuid(int gameuid) {
		this.gameuid = gameuid;
	}

	private UsersModel getUserInfoFromPassword() throws Exception {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where password='%s'", secGameuid));
		if (!obj.isEmpty()) {
			userName = obj.getString("username");
			gameuid = obj.getInt("id");
			client = obj.getInt("client_id");
		}
		return this;
	}

	public JSONArray getUserDevice(int clientid) throws Exception {
		String sql = String.format(
				"select * from user_device where client='%s'", clientid);
		return JDBC.select(sql);
	}

	/**
	 * 更新温度。
	 * 
	 * @param value
	 * @param port
	 * @return
	 */
	public boolean updateTemperature(double value, int port) {
		try {
			int time = getTime();
			String sql = String
					.format("update user_device set `values`='%f',`updatetime`=%d where client=%d and prot=%d",
							value, time, gameuid, port);
			System.out.println(sql);
			JDBC.query(sql);
			if (value > LIMIT) {
				sendToPush(this.gameuid, "温度提醒", "现在的温度是" + value + "请知晓！");
			}
		} catch (Exception e) {

		}
		return false;
	}

	public static int getAndroidId(int clientid) {
		String sql = String.format("select id from users where client_id= %d ",
				clientid);
		JSONObject obj = new JSONObject();
		try {
			obj = JDBC.selectOne(sql);
		} catch (Exception e) {
			Log.error(e.toString());
		}
		if (obj.containsKey("id")) {
			return obj.getInt("id");
		}
		return 0;
	}

	public static JSONObject getInfo(int id) {
		JSONObject obj = new JSONObject();
		return obj;
	}
}