package com.centurywar.control;

import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.ArduinoModel;
import com.centurywar.DeviceModel;
import com.centurywar.Main;
import com.centurywar.Redis;
import com.centurywar.UsersModel;

public class ArduinoControl {
	protected final static Log Log = LogFactory.getLog(ArduinoControl.class);

	public static String controlArduinoSend(String message, int id, int fromid) {
		// 更新上传信息
		ArduinoModel.updateDateTran(message.length(), 0, id);
		if (message.substring(0, 1).equals("r")) {
			ArduinoControl.controlReturn(id, message);
			return "";
		}
		String[] temp = null;
		temp = message.trim().split("_");
		if (temp.length < 4) {
			System.out.println("参数输入错误：" + message);
			Main.socketWriteAll(id, id, "error message", false,
					ConstantControl.WRITE_ARDUINO_HANDLER);
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
		// 重置所有板子所有状态
		if (temp[0].equals(ConstantControl.DEVICE_RESET)) {
			ArduinoModel.resetAllDevice(id);
		}

		// 这是人体传感器
		if (temp[0].equals(ConstantControl.DEVICE_RENTI)) {
			rentiControl(id);
		}
		return "";
	}

	public static void rentiControl(int fromid) {
		ArduinoModel u = new ArduinoModel(fromid);
		int usersGameuid = u.getUsersId();
		UsersModel user = new UsersModel(usersGameuid);
		if (user.mode == ConstantControl.MODE_DEFAULT) {
			UsersModel.openPMW(fromid, 30);
		} else if (user.mode == ConstantControl.MODE_DEFAULT) {
			ArduinoModel.sendToPush(fromid, "家里有人回来了", "家里有人了");
		}
	}

	/**
	 * 执行板子操作,仅此这一个地方
	 * 
	 * @param gameuid
	 * @param control
	 */
	public static void doCommand(int gameuid, String control) {
		// 先判断之前是否有相反的操作，如果有，先把该操作清除
		String keyStr = getCatchKey(gameuid);
		Set<String> cachedKeys = Redis.hkeys(keyStr);
		String controlarr[] = control.split("_");
		// for循环遍历：
		for (String key : cachedKeys) {
			String temarr[] = key.split("_");
			if (controlarr[0].equals(temarr[0])
					&& controlarr[1].equals(temarr[1])) {
				Redis.hdel(keyStr, key);
				break;
			}
		}
		Integer time = new Integer((int) (System.currentTimeMillis() / 1000));
		Redis.hset(keyStr, control, time.toString());
		doWriteToArduino(gameuid, control);
	}

	/**
	 * 初次登录的时候处理所有的信息
	 * 
	 * @param gameuid
	 */
	public static void doAllCommand(int gameuid) {
		String keyStr = getCatchKey(gameuid);
		Set<String> cachedKeys = Redis.hkeys(keyStr);
		// for循环遍历：
		for (String key : cachedKeys) {
			doWriteToArduino(gameuid, key);
			try {
				Thread.sleep(3);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	private static String getCatchKey(int gameuid) {
		return gameuid + "_command";
	}


	/**
	 * 处理板子返回的信息
	 * 
	 * @param message
	 * @return
	 */
	public static long controlReturn(int gameuid, String message) {
		String originCommand = message.substring(2);
		// 把反馈回传到客户端
		JSONObject obj = new JSONObject();
		obj.put("command", message);
		obj.put("gameuid", gameuid);
		EchoSetStatus.betch(obj);
		System.out.print(String.format("得到%d反馈，删除缓存：", gameuid, originCommand));
		return Redis.hdel(getCatchKey(gameuid), originCommand);
	}

	private static void doWriteToArduino(int gameuid, String control) {
		Main.socketWriteAll(gameuid, gameuid, control, false,
				ConstantControl.WRITE_ARDUINO_HANDLER);
		System.out.println(String.format("写入板子%d的内容:%s", gameuid, control));
		// 更新下载的信息
		ArduinoModel.updateDateTran(0, control.length(), gameuid);
	}
}
