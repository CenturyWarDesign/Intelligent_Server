package com.centurywar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import com.centurywar.control.ArduinoControl;

//定时运行的程序
public class TimingTask extends TimerTask {

	/**
	 * 从数据为库中查出要定时运行的程序
	 */
	public static void getTimingBehave() {

	}

	public static void insertTimingBehave() {
		// Behave be = new Behave();

	}

	public void run() {
		Behave be = new Behave(0);
		// 检测缓存中有木有超时需要加入冲发表的
		// this.checkCachedCommands(6, be);
		List<Behave> needsend = be.getNeedRunInfo();
		List<Integer> hasSend = new ArrayList<Integer>();
		for (int i = 0; i < needsend.size(); i++) {
			Behave tembe = needsend.get(i);
			ArduinoControl.doCommand(tembe.gameuid, tembe.behaveString);
			hasSend.add(tembe.id);
		}
		be.delInfo(hasSend);
		System.out.println("定时操作发送中……");
	}

	public void clearSocket() {
		System.out.println("清空没用的连接。。。。");
		Main.clearTem();
		Main.clearArduino();
		Main.clearAndroid();
	}

	/**
	 * 检查缓存中超时的命令，写入send_log表
	 * 
	 * @param timeout
	 *            设置超时时间 单位为秒
	 * @param behave
	 *            behave对象，随时写入重发表
	 */
	public void checkCachedCommands(int timeout, Behave behave) {
		// 取出缓存的集合
		Set<String> cachedKeys = Redis.hkeys("cachedCommands");
		// for循环遍历：
		for (String key : cachedKeys) {
			String timeStr = Redis.hget("cachedCommands", key);
			System.out.println(timeStr);
			Integer sendTime = Integer.parseInt(timeStr);
			Integer nowTime = new Integer(
					(int) (System.currentTimeMillis() / 1000));
			if (nowTime - sendTime > timeout) {
				System.out.println("反馈超时，写入重发表:" + key);
				Redis.hdel("cachedCommands", key);
				String params[] = key.split(":");
				behave.newInfo(Integer.parseInt(params[0]), 11, sendTime,
						params[1]);
			}
		}
	}
}
