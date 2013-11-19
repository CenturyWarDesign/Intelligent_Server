package com.centurywar;

import redis.clients.jedis.Jedis;


public class Redis {
	// Redis,缓存数据库，对于一些频繁的查询要用
	protected static Jedis redis;

	private static void initRedis() {
		if (redis == null) {
			redis = new Jedis("127.0.0.1", 6379);
		}
	}

	
	/**
	 * 设置字段至Redis
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String set(String key, String value) {
		initRedis();
		return redis.set(key, value);
	}

	/**
	 * 从Redis读取
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String get(String key) {
		initRedis();
		return redis.get(key);
	}
}