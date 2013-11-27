package com.centurywar;

import net.sf.json.JSONObject;

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

}
