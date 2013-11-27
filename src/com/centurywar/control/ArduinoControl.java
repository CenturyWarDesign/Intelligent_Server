package com.centurywar.control;

import com.centurywar.ArduinoModel;
import com.centurywar.Main;

public class ArduinoControl {
	public static String controlArduinoSend(String message, int id, int fromid) {
		String[] temp = null;
		temp = message.trim().split("_");
		// 4个是标准输入
		if (temp[0].equals("r")) {
			return "";
		}
		if (temp[0].equals(ConstantControl.DEVICE_TEMPERATURE)) {
			double temValue = 0.0;
			temValue = Float.parseFloat(temp[2] + "." + temp[3]);
			// 温度计返回值
			ArduinoModel u = new ArduinoModel(id);
			u.updateTem(temValue, 1);
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
