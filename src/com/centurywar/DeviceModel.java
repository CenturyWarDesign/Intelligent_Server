package com.centurywar;

import net.sf.json.JSONObject;

import com.centurywar.control.ConstantControl;

public class DeviceModel extends BaseModel {
	public int pik = 0;
	public String name = "";
	public double value;
	public int updatetime;
	public int arduinoid;

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
	 * 处理板子返回的信息
	 * 
	 * @param commandReturn
	 * @param arduinoid
	 * @return
	 */
	public static boolean updateDeviceByReturn(String commandReturn,
			int arduinoid) {
		JSONObject temobj = getJSONStrFromCommand(commandReturn);
		String sql = String
				.format("update user_device set value=%d where arduinoid=%d and pik=%d;",
						temobj.getInt("value"), arduinoid, temobj.getInt("pik"));
		JDBC.query(sql);
		return true;
	}

	public static JSONObject getJSONStrFromCommand(String command) {
		if (command.substring(0, 1).equals("r")) {
			command = command.substring(2);
		}
		String[] comArr = command.split("_");
		JSONObject obj = new JSONObject();
		obj.put("type", comArr[1]);
		obj.put("pik", comArr[2]);
		if (comArr[0].equals(ConstantControl.DEVICE_LIGTH)
				|| comArr[0].equals(ConstantControl.DEVICE_PMW)) {
			obj.put("value", comArr[3]);
		} else if (comArr[0].equals(ConstantControl.DEVICE_TEMPERATURE)) {
			obj.put("value",
					Float.parseFloat(comArr[2] + "." + comArr[3].trim()));
		}
		return obj;
	}
}
