package com.centurywar;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.control.ConstantControl;

public class Behave extends BaseModel {
	protected final static Log Log = LogFactory.getLog(BaseModel.class);
	public int id;
	public int gameuid;
	public int fromgameuid;
	public String behaveString;
	public int time;
	public int sendtime;
	public int status;

	public Behave(int id) {
		if (id > 0) {
			getInfo();
		}
	}


	private Behave getInfo() {
		JSONObject rs = null;
		rs = JDBC.selectOne(String.format(
				"select * from send_log where id='%s'", id));
		if (!rs.isEmpty()) {
			gameuid = rs.getInt("gameuid");
			fromgameuid = rs.getInt("fromgameuid");
			time = rs.getInt("time");
			sendtime = rs.getInt("sendtime");
			status = rs.getInt("status");
		}
		return this;
	}


	public ArrayList<Behave> getNeedRunInfo() {
		ArrayList<Behave> temlist = new ArrayList<Behave>();
		JSONArray rs = JDBC.select(String.format(
				"select * from send_log where time<%d ",
				getTime()));
		for (int i = 0; i < rs.size(); i++) {
			JSONObject obj = rs.getJSONObject(i);
			Behave be = new Behave(0);
			be.id = obj.getInt("id");
			be.gameuid = obj.getInt("gameuid");
			be.fromgameuid = obj.getInt("fromgameuid");
			be.time = obj.getInt("time");
			be.sendtime = obj.getInt("sendtime");
			be.status = obj.getInt("status");
			be.behaveString = obj.getString("behaveString");
			temlist.add(be);
		}
		return temlist;
	}

	public boolean delInfo(int id) {
		try {
			JDBC.query(String.format("delete from send_log where id=%d", id));
			return true;
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
		}
		return false;
	}

	public boolean delInfo(List<Integer> del) {
		if (del.size() == 0) {
			return true;
		}
		try {
			StringBuffer delstr = new StringBuffer();
			for (int i = 0; i < del.size(); i++) {
				delstr.append(del.get(i) + ",");
			}
			String sql = String.format(
					"delete from send_log where id in (%s)",
					delstr.toString().substring(0,
							delstr.toString().length() - 1));
			JDBC.query(sql);
			System.out.println("[del_sql]" + sql);
			return true;
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
		}
		return false;
	}

	public boolean delayInfo(int id, int second) {
		try {
			JDBC.query(String.format(
					"update send_log set sendtime+=%d where id='%d'", second,
					id));
			return true;
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
		}
		return false;
	}

	public boolean newInfo(int gameuid, int fromgameuid, int sendtime,
			String behaveString) {
		try {
			int time = getTime();
			if (sendtime == 0) {
				sendtime = time;
			}
			if (sendtime < 0) {
				sendtime = time - sendtime;
			}
			return JDBC
					.query(String
							.format("insert into send_log(gameuid,fromgameuid,time,sendtime,status,behaveString) values (%d,%d,%d,%d,0,'%s');",
									gameuid, fromgameuid, sendtime, time,
									behaveString));
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
		}
		return false;
	}

	/**
	 * 向板子发送指令
	 * 
	 * @param gameuid
	 * @param behaveString
	 */
	public static void sendBehave(int gameuid, String behaveString) {
		Main.socketWriteAll(gameuid, gameuid, behaveString, false,
				ConstantControl.WRITE_ARDUINO_HANDLER);
	}

	/**
	 * 向板子发送延迟指令
	 * 
	 * @param gameuid
	 * @param behaveString
	 * @param delaysocend
	 */
	public static void sendBehave(int gameuid, String behaveString,
			int delaysocend) {

		Behave be = new Behave(0);
		be.newInfo(gameuid, gameuid, -Math.abs(delaysocend), behaveString);
	}
}
