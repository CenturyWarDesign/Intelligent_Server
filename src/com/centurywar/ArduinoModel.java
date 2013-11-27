package com.centurywar;

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

	public ArduinoModel(String sec){
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where  sec='%s'", sec));
		JsonToArduino(obj);
	}

	public ArduinoModel(int id)  {
		JSONObject obj = JDBC.selectOne(String.format(
				"select * from users where id=%d", id));
		JsonToArduino(obj);
	}

	private void JsonToArduino(JSONObject obj) {
		if (!obj.isEmpty()) {
			id = obj.getInt("id");
			port = obj.getInt("port");
			ip = obj.getString("ip");
		}
	}
	
	public boolean updateTem(double tem,int satatus){
		
		return true;
	}

	public int getUsersId(){
		return 100;
	}
	

}
