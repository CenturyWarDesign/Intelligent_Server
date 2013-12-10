package com.centurywar;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ArduinoModel extends BaseModel {
	protected final static Log Log = LogFactory.getLog(ArduinoModel.class);
	public final static double LIMIT = 100;
	public int id = 0;
	public String ip = "0.0.0.0";
	public int port = 0;
	public String bluetoothMac = "";
	// 用户设备表
	public List<DeviceModel> deviceList = new ArrayList<DeviceModel>();
	
	/**
	 * 设置最后一次连接时间
	 * 
	 * @param time
	 * @param arduinoid
	 */
	public static void setLastConnTime(int time, int arduinoid) {
		Redis.set(getLastTimeKey(arduinoid), time + "");
	}

	private static String getLastTimeKey(int arduinoid) {
		return String.format("last_conn_key_%d", arduinoid);
	}
	/**
	 * 得到最后一次连接时间
	 * @param arduinoid
	 */
	public static int  getLastConnTime(int arduinoid) {
		String str = Redis.get(getLastTimeKey(arduinoid));
		int lasttime = Integer.valueOf(str).intValue();
		if (lasttime == 0) {
			JSONObject obj = JDBC
					.selectOne(String
							.format("select max(conntime) maxid from arduino_conn_log where  arduinoid=%d",
									arduinoid));
			lasttime = obj.getInt("maxid");
			setLastConnTime(lasttime, arduinoid);
		}
		return lasttime;
	}
	
	public ArduinoModel(String sec) {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from arduino where  sec='%s'", sec));
		JsonToArduino(obj);
		if (!obj.isEmpty()) {
			id = obj.getInt("id");
			getConnLog(id);
		}
	}

	public ArduinoModel(int id) {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from arduino where id=%d", id));
		JsonToArduino(obj);
	}

	private void JsonToArduino(JSONObject obj) {
		if (!obj.isEmpty()) {
			id = obj.getInt("id");
			port = obj.getInt("port");
			ip = obj.getString("ip");
		}
	}

	public boolean updateTem(double tem, int satatus) {

		return true;
	}

	/**
	 * 取得一个arduino的用户信息
	 * 
	 * @return
	 */
	public int getUsersId() {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where client_id=%d", id));
		return obj.getInt("id");
	}

	/**
	 * 取得所有传感器信息
	 * 
	 * @return
	 */
	public JSONArray getDevice() {
		JSONArray obj = JDBC.select(String.format(
				"select * from user_device where arduinoid=%d", id));
		return obj;
	}
	
	/**
	 * 初始化用户设备表
	 */
	public void initDevice() {

	}

	/**
	 * 取得所有的传感器信息
	 * 
	 * @param id
	 * @return
	 */
	public static JSONArray getAllDevice(int id) {
		JSONArray jsonArray = JDBC
				.select("select * from user_device where arduinoid=" + id);
		return jsonArray;
	}

	/**
	 * 更新所有传感器信息
	 * 
	 * @param id
	 * @param jsonarr
	 * @return
	 */
	public static boolean updateAllDevice(int id, JSONArray jsonarr) {
		JDBC.query("delete from user_device where arduinoid=" + id);
		String sql = "";
		List<Integer> pikArray = new ArrayList<Integer>();
		for (int i = 0; i < jsonarr.size(); i++) {
			JSONObject tem = jsonarr.getJSONObject(i);
			if (pikArray.contains(tem.getInt("pik"))) {
				continue;
			}
			pikArray.add(tem.getInt("pik"));
			tem.put("arduinoid", id);
			sql += JDBC.insertStringFromJSONObject(tem, "user_device");
		}
		JDBC.query(sql);
		return true;
	}

	
	/**
	 * 添加登录的LOG信息
	 * @param arduinoid
	 */
	private static void getConnLog(int arduinoid) {
		int time = getTime();
		String sql = String
				.format("insert into `arduino_conn_log` (arduinoid,conntime,updatetime) values (%d,%d,%d)",
						arduinoid, time, time);
		JDBC.query(sql);
		setLastConnTime(time,arduinoid);
	}
	// public static boolean sendTo
	
	public static void updateDateTran(int up, int down, int arduinoid) {
		int conntime = getLastConnTime(arduinoid);
		int time = getTime();
		String sql = String
				.format("update arduino_conn_log set up=up+%d,down=down+%d,updatetime=%d where arduinoid=%d and conntime =%d",
						up, down, time, arduinoid, conntime);
		JDBC.query(sql);
	}
}
