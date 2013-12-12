package com.centurywar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.centurywar.control.ConstantControl;

public class UsersModel extends BaseModel {
	public final static double LIMIT = 100;
	public int gameuid = 0;

	private String secGameuid = "";
	public String userName = "";
	public int client = 0;
	public int mode = 1;
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
		gameuid = gameuidsend;
		getUserInfoFromGameuid();
	}
	
	public UsersModel(String username, String password) {
		JSONObject obj = JDBC
				.selectOne(String
						.format("select * from users where username='%s' and (password='%s' or sec='%s')",
								username, password, password));
		if (!obj.isEmpty()) {
			userName = obj.getString("username");
			gameuid = obj.getInt("id");
			client = obj.getInt("client_id");
		}
	}
	
	/**
	 * 更新板子的ＩＰ
	 * 
	 * @param ip
	 * @param port
	 */
	public static void updateIpAndPort(String ip, int port, int id) {
		int time = getTime();
		String sql = String.format(
				"update users set ip='%s',port =%d ,last_login=%d where id=%d",
				ip, port, time, id);
		JDBC.query(sql);
	}

	private UsersModel getUserInfoFromGameuid() {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where id=%d", gameuid));
		initUserInfoFromJSONObject(obj);
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
		initUserInfoFromJSONObject(obj);
		return this;
	}

	public void initUserInfoFromJSONObject(JSONObject obj) {
		if (!obj.isEmpty()) {
			userName = obj.getString("username");
			gameuid = obj.getInt("id");
			client = obj.getInt("client_id");
			mode = obj.getInt("mode");
		}
	}

	public JSONArray getUserDevice(int clientid) throws Exception {
		String sql = String.format(
				"select * from user_device where arduinoid='%s'", clientid);
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
		String sql = String.format("select client_id from users where id= %d ",
				clientid);
		JSONObject obj = new JSONObject();
		obj = JDBC.selectOne(sql);
		return obj.getInt("client_id");
	}

	public static JSONObject getInfo(int id) {
		String sql = String.format("select * from users where id= %d ", id);
		JSONObject obj = JDBC.selectOne(sql);
		return obj;
	}

	public static int getUserIdFromClientid(int clientid) {
		String sql = String.format("select * from users where client_id= %d ",
				clientid);
		JSONObject obj = JDBC.selectOne(sql);
		return obj.getInt("id");
	}

	public boolean UpdateUserDevice(JSONArray deviceArray) {
		ArduinoModel.updateAllDevice(client, deviceArray);
		// String sqldel =
		// String.format("delete from user_device where id=%d",id);
		return true;
	}

	/**
	 * 返回用户所有的传感器
	 * 
	 * @return
	 */
	public static JSONArray getAllUserDevice(int id) {
		int arduinoid = getAndroidId(id);
		return ArduinoModel.getAllDevice(arduinoid);
	}

	/**
	 * 打开用户PMW灯
	 * 
	 * @param arduinoid
	 * @param delay
	 */
	public static void openPMW(int arduinoid, int delay) {
		String sql = String
				.format("select pik from user_device where arduinoid= %d and type=20 limit 1",
						arduinoid);
		JSONObject obj = JDBC.selectOne(sql);
		int pik = obj.getInt("pik");
		String openStr = getBehaver(20, pik, 1, 0);
		String closeStr = getBehaver(20, pik, 0, 0);
		Behave.sendBehave(arduinoid, openStr);
		Behave.sendBehave(arduinoid, closeStr, delay);
	}

	/**
	 * 向客户端发送错误数据值
	 * 
	 * @param code
	 * @param gameuid
	 */
	public static void sendError(int code, int gameuid) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("control", ConstantControl.ECHO_SERVER_MESSAGE);
		Main.socketWriteAll(gameuid, gameuid, obj.toString(), false,
				ConstantControl.WRITE_GLOBAL_HANDLER);
	}

	/**
	 * 向Tem客户端发送错误数据值
	 * 
	 * @param code
	 * @param gameuid
	 */
	public static void sendErrorTem(int code, int gameuid) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("control", ConstantControl.ECHO_SERVER_MESSAGE);
		Main.socketWriteAll(gameuid, gameuid, obj.toString(), false,
				ConstantControl.WRITE_TEM_HANDLER);
	}
}
