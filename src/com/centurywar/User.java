package com.centurywar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class User extends BaseModel {
	public final static double LIMIT = 50;
	private int gameuid = 0;
	
	private String secGameuid = "";
	public String userName = "";
	public String ip = "0.0.0.0";
	public int port = 80;
	public int client = 0;
	public User(String sec) {
		secGameuid = sec;
		try{
			getUserInfoFromPassword();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User(int gameuidsend) {
		if (gameuidsend > 0) {
			gameuid = gameuidsend;
			try {
				getUserInfoFromGameuid();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private User getUserInfoFromGameuid() throws Exception {
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


	private User getUserInfoFromPassword() throws Exception {
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
			if(value>LIMIT){
				sendToPush(this.gameuid, "温度提醒", "现在的温度是" + value + "请知晓！");
			}
		} catch (Exception e) {

		}
		return false;
	}


}
