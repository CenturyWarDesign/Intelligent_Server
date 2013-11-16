package com.centurywar;

import java.util.Date;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

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
	public boolean sendToPush(int gameuid,String title, String content) {
		JPushClient jpush = new JPushClient("68c01e4660be175e19844c47",
				"550943fb320cf916b5a78c41");
		MessageResult msgResult = jpush.sendNotificationWithAlias(11221,
				"caojunling", title, content);
		if (null != msgResult) {
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode()
						+ ", 错误消息=" + msgResult.getErrmsg());
				return false;
			}
		} else {
			System.out.println("无法获取数据");
			return false;
		}
		return true;
	}

	public int getFromgameuid() {
		return fromgameuid;
	}

	public void setFromgameuid(int fromgameuid) {
		this.fromgameuid = fromgameuid;

	}

	public BaseModel() {
		// TODO Auto-generated constructor stub'
	}


}
