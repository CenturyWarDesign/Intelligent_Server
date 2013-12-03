package com.centurywar.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.ArduinoModel;
import com.centurywar.DeviceModel;
import com.centurywar.Main;

public class ArduinoControl {
	protected final static Log Log = LogFactory.getLog(ArduinoControl.class);

	public static String controlArduinoSend(String message, int id, int fromid) {
		String[] temp = null;
		temp = message.trim().split("_");
		// 4个是标准输入
		if (temp[0].equals("r")) {
			return "";
		}
		if (temp[0].equals(ConstantControl.DEVICE_TEMPERATURE)) {
			DeviceModel.updateDeviceByReturn(message, id);
		}
		// 这是火焰报警器
		if (temp[0].equals(ConstantControl.DEVICE_HUOJING)) {
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWriteAll(id, id, "error message", false,
						ConstantControl.WRITE_ARDUINO_HANDLER);
				return "";
			}
			ArduinoModel u = new ArduinoModel(fromid);
		}
		// 这是人体传感器
		if (temp[0].equals(ConstantControl.DEVICE_RENTI)) {
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWriteAll(id, id, "error message", false,
						ConstantControl.WRITE_ARDUINO_HANDLER);
				return "";
			}
			ArduinoModel u = new ArduinoModel(fromid);
			u.sendToPush(id, "家里有人回来了", "家里有人了");
		}
		return "";
	}
}
