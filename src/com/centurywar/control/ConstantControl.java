package com.centurywar.control;

public class ConstantControl {
	// 检查用户名密码
	public static String CHECK_USERNAME_PASSWORD = "cup";
	public static String ECHO_CHECK_USERNAME_PASSWORD = "ecup";
	
	
	// 设置用户传感器状态
	public static String SET_STATUS = "ss";
	public static String ECHO_SET_STATUS = "ess";

	// 取得用户温度信息
	public static String GET_USER_TEMPERATURE = "gut";
	public static String ECHO_GET_USER_TEMPERATURE = "egut";
	
	// 取得用户温度信息
	public static String GET_USER_INFO = "gui";
	public static String ECHO_GET_USER_INFO = "egui";

	// ========================传感器=========================

	// 灯
	public static String DEVICE_LIGTH = "10";
	// PMW
	public static String DEVICE_PMW = "20";
	// 温度传感器
	public static String DEVICE_TEMPERATURE = "30";
	// 火警传感器
	public static String DEVICE_HUOJING = "31";
	// 人体传感器
	public static String DEVICE_RENTI = "32";

	// 控制指令（必须要有以下内容；type,pik,value,data）
	public static String CONTROL_DEVICE = "cd";
	public static String ECHO_CONTROL_DEVICE = "ecd";
}
