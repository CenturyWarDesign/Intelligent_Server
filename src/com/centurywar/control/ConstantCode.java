package com.centurywar.control;

public class ConstantCode {
	// 连接正常
	public static int CODE_CONNECTET = 10000;

	// 连接异常
	public static int CODE_CONNECTET_ERROR = 10001;

	// 用户名或密码错误
	public static int USER_OR_PASSWORD_ERROR = 20001;

	// 用户名用户多开
	public static int USER_MORE_THAN_ONE_ERROR = 20002;

	// 用户名或密码不可用
	public static int USER_OR_PASSWORD_CANT_USE = 20003;
	// 用户注册成功
	public static int USER_REG_SUCCESS = 20004;

	// arduino 登录
	public static int USER_ARDUINO_LOGIN = 30001;
	// arduino 设置模式
	public static int USER_Mode_UPDATE_OK = 30201;
	public static int USER_Mode_UPDATE_FAIL = 30202;
}
