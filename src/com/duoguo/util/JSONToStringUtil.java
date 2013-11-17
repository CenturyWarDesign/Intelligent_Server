package com.duoguo.util;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.PUBLIC_MEMBER;

import net.sf.json.JSONObject;

public class JSONToStringUtil {

	public static Map<String,String> JSON2Map(JSONObject json){
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", "liuchunlong");
		map.put("age", "26");
		map.put("gender", "男");
		System.out.println(json.fromObject(map));
		return null;
	}
	public static void main(String[] args) {
		JSONToStringUtil.JSON2Map(new JSONObject());
	}
}
