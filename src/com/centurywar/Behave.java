package com.centurywar;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Behave extends BaseModel {
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
		try {
			ResultSet rs = JDBC.select(String.format(
					"select * from send_log where id='%s'", id));
			while (rs.next() && rs != null) {
				behaveString = rs.getString("username");
				gameuid = rs.getInt("gameuid");
				fromgameuid = rs.getInt("fromgameuid");
				time = rs.getInt("time");
				sendtime = rs.getInt("sendtime");
				status = rs.getInt("satus");
				return this;
			}
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
		}
		return this;
	}

	public List<Behave> getNeedRunInfo() {
		List<Behave> temlist = new ArrayList<Behave>();
		try {
			ResultSet rs = JDBC.select(String.format(
					"select * from send_log where time<%d", getTime()));
			while (rs.next() && rs != null) {
				Behave tem = new Behave(0);
				tem.id = rs.getInt("id");
				tem.gameuid = rs.getInt("gameuid");
				tem.fromgameuid = rs.getInt("fromgameuid");
				tem.behaveString = rs.getString("behaveString");
				tem.status = rs.getInt("status");
				temlist.add(tem);
			}
		} catch (Exception e) {
			System.out.println("[send_log]" + e);
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


}
