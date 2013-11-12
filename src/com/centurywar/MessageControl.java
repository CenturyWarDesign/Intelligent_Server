package com.centurywar;

/**
 * @author Administrator 接收板子的请求字符串，进行数据分发组装。
 *         message格式：传感器类型_引脚_值_附加位（若值是温度20.5，则值为20，附加位为5）
 */
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
		
		if (temp[0].equals("30"))
 {
			double temValue = 0.0;
			temValue = Integer.parseInt(temp[2]) + Float.parseFloat(temp[3])
					/ 100;
			// 温度计返回值
			if (gameuid == 0) {
				gameuid = fromgameuid;
			}
			User u = new User(gameuid);
			u.setFromgameuid(fromgameuid);
			u.updateTemperature(temValue, 1);
		}

		/*
		 * 2013年11月4日 jlcao
		 */
		
		if (temp[0].equals("control")) {
			 controlBetch(temp[1],message,gameuid,fromgameuid);
			 return "";
		}
		//control_cup_username_password
		if (temp.length < 4) {
			System.out.println("参数输入错误：" + message);
			Main.socketWrite(fromgameuid, gameuid, "error message", false);
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
	
	public static void controlBetch(String command,String control,int gameuid,int fromgameuid){
		if(command.equals("cup")){
			CheckPassword.betch(String.format("%s_%d_%d", control,gameuid,fromgameuid));
		}
		
		
	}
}
