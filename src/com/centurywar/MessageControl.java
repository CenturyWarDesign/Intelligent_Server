package com.centurywar;

public class MessageControl {
	public static String MessageControl(String message, int gameuid,int fromgameuid) {
		String[] temp = null;
		temp = message.trim().split("_");
		int type = 0;
		int pik = 0;
		int command = 0;
		int value = 0;
		// 4个是标准输入
		if (temp[0].equals("r")) {
			return "";
		}
		if (temp.length < 4) {
			System.out.println("参数输入错误：" + message);
			return "";
		}

		type = Integer.parseInt(temp[0]);
		pik = Integer.parseInt(temp[1]);
		command = Integer.parseInt(temp[2]);
		value = Integer.parseInt(temp[3]);
		// 有延时的开关
		if (type == 10 && value > 0) {
			Behave be = new Behave(0);
			be.newInfo(gameuid, fromgameuid, -value,
					getBehaver(type, pik, command, value));
			return "";
		}
		return getBehaver(type, pik, command, 0);

	}

	public static String getBehaver(int type, int pik, int commmand, int value) {
		return String.format("%d_%d_%d_%d", type, pik, commmand, value);
	}
}
