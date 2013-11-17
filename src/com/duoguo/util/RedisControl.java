package com.duoguo.util;

import java.io.IOException;
import java.util.Date;
import redis.clients.jedis.Jedis;
import net.sf.json.JSONObject;

public class RedisControl {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		int Max = 10000000;
		int begin = getTime();
		Jedis redis = new Jedis("127.0.0.1", 6379);
		for (int i = 0; i < Max; i++) {
			redis.set(i + "", i * i + "");
		}
		System.out.println("MAX:"+Max);
		System.out.println("Time:" + (getTime() - begin));
	}

	public static int getTime() {
		Date date = new Date();
		return (int) (date.getTime() / 1000);
	}
}
