package com.centurywar;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

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
		List<Behave> needsend = be.getNeedRunInfo();
		List<Integer> hasSend = new ArrayList<Integer>();
		for (int i = 0; i < needsend.size(); i++) {
			Behave tembe = needsend.get(i);
			System.out.println("正在发送：" + tembe.behaveString);
			if (Main.socketWrite(tembe.gameuid, tembe.fromgameuid,
					tembe.behaveString)) {
				hasSend.add(tembe.id);
				System.out.println("发送成功：" + tembe.behaveString + "id:"
						+ tembe.id);
			}
		}
		be.delInfo(hasSend);
		System.out.println("备份程序运行……");
	}
}
