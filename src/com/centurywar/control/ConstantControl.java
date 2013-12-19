package com.centurywar.control;

public class ConstantControl {
	// 检查用户名密码
	public static String CHECK_USERNAME_PASSWORD = "cup";
	public static String ECHO_CHECK_USERNAME_PASSWORD = "ecup";
	
	// 用户注册
	public static String REG_USERNAME_PASSWORD = "rup";
	public static String ECHO_REG_USERNAME_PASSWORD = "rcup";
	
	// 自动匹配客户端
	public static String AUTO_GET_ARUDINO_ID = "agai";
	public static String ECHO_AUTO_GET_ARUDINO_ID = "eagai";

	// 设置用户传感器状态
	public static String SET_STATUS = "ss";
	public static String ECHO_SET_STATUS = "ess";

	// 取得用户温度信息
	public static String GET_USER_TEMPERATURE = "gut";
	public static String ECHO_GET_USER_TEMPERATURE = "egut";
	
	// 取得用户温度信息
	public static String GET_USER_INFO = "gui";
	public static String ECHO_GET_USER_INFO = "egui";

	// 同步用户的信息到网上
	public static String UPDAT_DEVICE_TO_SERVER = "udts";
	public static String ECHO_UPDAT_DEVICE_TO_SERVER = "eudts";

	// 设置用户的模式
	public static String UPDAT_USER_MODE = "uum";
	public static String ECHO_UPDAT_USER_MODE = "euum";

	// 控制指令（必须要有以下内容；type,pik,value,data）
	public static String CONTROL_DEVICE = "cd";
	public static String ECHO_CONTROL_DEVICE = "ecd";

	// 输出服务器的错误信息
	public static String ECHO_SERVER_MESSAGE = "esm";
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
	// 光感
	public static String DEVICE_LIGHT_VALUE = "33";
	// 红外接收
	public static String DEVICE_RED_LIGHT_GET = "34";

	// 设置人体感应之后执行的内容
	public static String DEVICE_SET_RENTI_CONTROL = "51";
	// 设置人体感应持续时间
	public static String DEVICE_SET_RENTI_CONTROL_LAST = "52";

	// 设置光敏感应高于一定的值打开的引脚
	public static String DEVICE_SET_LIGHT_OPEN_CONTROL = "53";
	// 设置光敏感应低于一定的值打开的引脚
	public static String DEVICE_SET_LIGHT_CLOSE_CONTROL = "54";

	// 设置光敏感应高于的值
	public static String DEVICE_SET_LIGHT_CONTROL = "55";
	// 设置光敏感应低于的值
	public static String DEVICE_SET_LIGHT_CONTROL_FINISH = "56";

	// 设置温度感应高于一定的值打开的引脚
	public static String DEVICE_SET_TEM_OPEN_CONTROL = "57";
	// 设置温度感应低于一定的值打开的引脚
	public static String DEVICE_SET_TEM_CLOSE_CONTROL = "58";

	// 设置温度感应高于的值
	public static String DEVICE_SET_TEM_CONTROL = "59";
	// 设置温度感应低于的值
	public static String DEVICE_SET_TEM_CONTROL_FINISH = "60";

	// 重置所有状态
	public static String DEVICE_RESET = "00";

	// 正常模式
	public static int MODE_DEFAULT = 1;
	// 外出模式
	public static int MODE_OUT = 2;

	// ===================写入模式

	public static int WRITE_TEM_HANDLER = 1;
	public static int WRITE_ARDUINO_HANDLER = 2;
	public static int WRITE_GLOBAL_HANDLER = 3;

}
