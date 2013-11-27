package com.centurywar.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.ArduinoModel;
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
			double temValue = 0.0;
			try {
				temValue = Float.parseFloat(temp[2] + "." + temp[3].trim());
				// 温度计返回值
				ArduinoModel u = new ArduinoModel(id);
				u.updateTem(temValue, 1);
			} catch (Exception e) {
				Log.error(e.toString());
			}
		}
		// 这是火焰报警器
		if (temp[0].equals(ConstantControl.DEVICE_HUOJING)) {
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWrite(id, id, "error message", false);
				return "";
			}
			try {
				ArduinoModel u = new ArduinoModel(fromid);

			} catch (Exception e) {

			}
		}
		// 这是人体传感器
		if (temp[0].equals(ConstantControl.DEVICE_RENTI)) {
			if (temp.length < 4) {
				System.out.println("参数输入错误：" + message);
				Main.socketWrite(id, id, "error message", false);
				return "";
			}
			try {
				ArduinoModel u = new ArduinoModel(fromid);
				// u.sendToPush(id, "着火了", "家里有可能着火了");
			} catch (Exception e) {

			}
		}

		return "";
	}
}
