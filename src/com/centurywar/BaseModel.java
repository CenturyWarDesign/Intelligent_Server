package com.centurywar;

import java.util.Date;

public class BaseModel {
	private int fromgameuid	= 0;
	public int getTime() {
		Date date = new Date();
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 发送推送消息到手机客户端
	 * 
	 * @param gameuid
	 *            注意，这里写板子的id,里面会把所有手机的客户端都进行遍历的
	 * @param content
	 * @return
	 */
	public boolean sendToPush(int gameuid, String content) {
		
		return true;
	}

	public int getFromgameuid() {
		return fromgameuid;
	}

	public void setFromgameuid(int fromgameuid) {
		this.fromgameuid = fromgameuid;
	}
}
