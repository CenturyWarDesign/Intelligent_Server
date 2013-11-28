package com.centurywar.control;

//从板子返回的信息，发送到客户端，客户端更新状态
import java.io.IOException;

import net.sf.json.JSONObject;

import com.centurywar.ArduinoModel;
import com.centurywar.DeviceModel;

public class EchoSetStatus extends BaseControl {

	public EchoSetStatus() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void betch(JSONObject jsonObj) {
		int androidId = jsonObj.getInt("gameuid");
		String command = jsonObj.getString("command");
		ArduinoModel arduino = new ArduinoModel(androidId);
		int gameuid = arduino.getUsersId();
		DeviceModel.updateDeviceByReturn(command, androidId);

		JSONObject Jso = new JSONObject();
		Jso.put("gameuid", gameuid);
		Jso.put("fromgameuid", androidId);
		Jso.put("command", command);
		sendToSocket(Jso, ConstantControl.ECHO_SET_STATUS);
	}

}
