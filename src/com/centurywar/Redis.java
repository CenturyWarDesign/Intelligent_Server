package com.centurywar;

import java.util.Set;

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
	 * 设置字段至Redis,有过期时间
	 * 
	 * @param key
	 * @param value
	 * @param ex
	 * @return
	 */
	public static String set(String key, String value, int ex) {
		initRedis();
		return redis.setex(key, ex, value);
	}

	/**
	 * 缓存至集合
	 * 
	 * @param set
	 * @param key
	 * @param value
	 * @return
	 */
	public static long hset(String set,String key,String value){
		initRedis();
		return redis.hset(set, key, value);
	}
	
	/**
	 * 获取一个集合中所有的keys
	 * 
	 * @param set
	 * @return
	 */
	public static Set<String> hkeys(String set){
		initRedis();
		return redis.hkeys(set);
	}
	
	public static String hget(String set,String key){
		initRedis();
		return redis.hget(set, key);
	}
	
	/**
	 * 删除集合中一个或多个值
	 * 
	 * @param set
	 * @param key
	 * @return
	 */
	public static long hdel(String set,String key){
		initRedis();
		return redis.hdel(set, key);
	}
	
	/**
	 * 删除key
	 * 
	 * @param key
	 * @return
	 */
	public static long del(String key){
		initRedis();
		return redis.del(key);
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