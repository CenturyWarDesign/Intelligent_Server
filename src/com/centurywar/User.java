package com.centurywar;

import java.sql.ResultSet;

public class User extends BaseModel {
	public final static double LIMIT = 30;
	private int gameuid = 0;
	
	private String secGameuid = "";
	public String userName = "";
	public String ip = "0.0.0.0";
	public int port = 80;
	public int client = 0;
	public User(String sec) {
		secGameuid = sec;
		getUserInfo();
	}

	public User(int gameuidsend) {
		gameuid = gameuidsend;
		getUserInfoFromGameuid();
	}

	private User getUserInfoFromGameuid() {
		try {
			ResultSet rs = JDBC.select(String.format(
					"select * from users where id=%d", gameuid));
			while (rs.next() && rs != null) {
				userName = rs.getString("username");
				gameuid = rs.getInt("id");
				client = rs.getInt("client_id");
				return this;
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return this;

	}

	public int getGameuid() {
		return gameuid;
	}

	public void setGameuid(int gameuid) {
		this.gameuid = gameuid;
	}


	private User getUserInfo() {
		try {
			ResultSet rs = JDBC.select(String.format(
					"select * from users where password='%s'", secGameuid));
			while (rs.next() && rs != null) {
				userName = rs.getString("username");
				gameuid = rs.getInt("id");
				client = rs.getInt("client_id");
				return this;
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return this;

	}

	/**
	 * 更新温度。
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
				sendToPush(this.gameuid,"现在的温度是"+value+"请知晓！");
			}
		} catch (Exception e) {

		}
		return false;
	}
}
