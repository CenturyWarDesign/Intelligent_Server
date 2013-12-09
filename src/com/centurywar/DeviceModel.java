package com.centurywar;

import net.sf.json.JSONObject;

import com.centurywar.control.ConstantControl;

/**
 * 这个类主要功能就是管理用户传感器信息
 * 
 * @author wanhin
 * 
 */
public class DeviceModel extends BaseModel {
	public int pik = 0;
	public String name = "";
	public double value;
	public int updatetime;
	public int arduinoid;

	/**
	 * 通过anduinoid和pik可以定位唯一的传感器
	 * 
	 * @param arduinoid
	 * @param pik
	 */
	public DeviceModel(int arduinoid, int pik) {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from user_device where arduinoid=%d and pik=%d",
				arduinoid, pik));
		if (obj != null) {
			this.pik = obj.getInt("pik");
			this.name = obj.getString("name");
			this.arduinoid = obj.getInt("arduinoid");
			this.value = obj.getDouble("value");
			this.updatetime = obj.getInt("updatetime");
		}
	}
	
	/**
	 * 处理板子返回的信息,把数据 库进行更新
	 * 
	 * @param commandReturn
	 * @param arduinoid
	 * @return
	 */
	public static boolean updateDeviceByReturn(String commandReturn,
			int arduinoid) {
		JSONObject temobj = getJSONStrFromCommand(commandReturn);
		Double dou=temobj.getDouble("value");
		String sql = String
				.format("update user_device set value=%s where arduinoid=%d and pik=%d;",
						dou.toString(), arduinoid,
						temobj.getInt("pik"));
		JDBC.query(sql);
		return true;
	}

	/**
	 * 返回的是温度进行更新
	 * 
	 * @param commandReturn
	 * @param arduinoid
	 */
	public static void updateTem(String commandReturn, int arduinoid) {

	}

	/**
	 * 分板返回的数据，得到需要的数据
	 * 
	 * @param command
	 * @return
	 */
	public static JSONObject getJSONStrFromCommand(String command) {
		if (command.substring(0, 1).equals("r")) {
			command = command.substring(2);
		}
		String[] comArr = command.split("_");
		JSONObject obj = new JSONObject();
		obj.put("type", comArr[0]);
		obj.put("pik", comArr[1]);
		if (comArr[0].equals(ConstantControl.DEVICE_LIGTH)
				|| comArr[0].equals(ConstantControl.DEVICE_PMW)) {
			obj.put("value", comArr[2]);
		} else if (comArr[0].equals(ConstantControl.DEVICE_TEMPERATURE)) {
			obj.put("value",
 Float.parseFloat(comArr[2]));
		}
		return obj;
	}
}
