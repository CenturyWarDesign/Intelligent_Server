package com.centurywar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.control.ArduinoControl;
import com.centurywar.control.ConstantCode;
import com.centurywar.control.ConstantControl;
import com.centurywar.control.MessageControl;

public class Main {
	protected final static Log Log = LogFactory.getLog(Main.class);
	private int port = 8080;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 4000;
	public static Map<String, MainHandler> globalHandler = new HashMap<String, MainHandler>();
	public static Map<String, MainHandler> arduinoHandler = new HashMap<String, MainHandler>();
	public static Map<String, MainHandler> temHandler = new HashMap<String, MainHandler>();
	private static int MaxTem = 1;
	
	public Main() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		Log.info("服务启动，等待请求！");
		System.out.println("waiting for");
		// 注册定期运行任务
		Timer timer = new Timer();
		timer.schedule(new TimingTask(), 5000, 20000);
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("First get it " + socket.getInetAddress()
						+ ":" + socket.getPort());
				Log.info("收到用户的连接请求,地址为--" + socket.getInetAddress() + ":"
						+ socket.getPort());
				String sec = null;
				InputStream socketIn = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						socketIn));
				sec = br.readLine();
				//  判断是否为Ardnino登录
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 设置日期格式
				System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				if (sec.length() == 32) {
					System.out.println("Sec:" + sec);
					ArduinoModel arduinoModel = new ArduinoModel(sec);
					MainHandler temr = new MainHandler(socket,
							arduinoModel.id, false);
					executorService.execute(temr);
					arduinoHandler.put(arduinoModel.id + "", temr);

					// 把所有的指令下发
					ArduinoControl.doAllCommand(arduinoModel.id);
					System.out.println(String.format(
							"put in Arduino Haddle:%d now have: %d ",
							arduinoModel.id, arduinoHandler.size()));
				} else {
					MainHandler temr = new MainHandler(socket, MaxTem, true);
					executorService.execute(temr);
					clearTem();
					temHandler.put(MaxTem + "", temr);
					JSONObject obj = new JSONObject();
					obj.put("code", ConstantCode.CODE_CONNECTET);
					obj.put("control", ConstantControl.ECHO_SERVER_MESSAGE);
					pw.write(obj.toString());
					System.out.println(String.format("[send to Android]%s",
							obj.toString()));
					System.out.println(String.format(
							"put in Tem Haddle:%d now have:%d", MaxTem,
							temHandler.size()));
					MaxTem++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.toString());
				Log.error("用户发起连接出现异常", e);
			}
		}
	}


	public static void clearTem() {
		for (String key : temHandler.keySet()) {
			Socket temsoc = temHandler.get(key).socket;
			if (!temsoc.isConnected()) {
				try {
					temsoc.close();
				} catch (Exception e) {
					System.out.println("tem socket " + key + "is closed");
				}
				temHandler.remove(key);
			}
		}
	}

	public static void clearArduino() {
		for (String key : arduinoHandler.keySet()) {
			Socket temsoc = arduinoHandler.get(key).socket;
			if (!temsoc.isConnected()) {
				try {
					temsoc.close();
				} catch (Exception e) {
					System.out.println("arduino socket " + key + "is closed");
				}
				arduinoHandler.remove(key);
			}
		}
	}

	public static void clearAndroid() {
		for (String key : globalHandler.keySet()) {
			Socket temsoc = globalHandler.get(key).socket;
			if (!temsoc.isConnected()) {
				try {
					temsoc.close();
				} catch (Exception e) {
					System.out.println("android socket " + key + "is closed");
				}
				globalHandler.remove(key);
			}
		}
	}


	

	public static boolean socketWriteAll(int id, int fromid, String content,
			boolean resend, int writetype) {
		if (id <= 0) {
			return false;
		}
		Map<String, MainHandler> tempHandler = null;
		String writeStr = "";
		if (writetype == ConstantControl.WRITE_ARDUINO_HANDLER) {
			tempHandler = arduinoHandler;
			writeStr = "Arduino";
		} else if (writetype == ConstantControl.WRITE_GLOBAL_HANDLER) {
			tempHandler = globalHandler;
			writeStr = "Global";
		} else {
			tempHandler = temHandler;
			writeStr = "Tem";
		}
		if (tempHandler.containsKey(id + "")) {
			MainHandler temHandler = tempHandler.get(id + "");
			try {
				OutputStream socketOut = temHandler.socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				pw.println(content);
				Log.info(String.format("服务端写向%s客户端%d报文为：%s", writeStr, id,
						content));
				return true;
			} catch (Exception e) {
				// 记录失败的程序
				e.printStackTrace();
				// 把socket给移除
				MainHandler tem = tempHandler.get(id + "");
				if (tem.socket.isClosed()) {
					try {
						tem.socket.close();
						globalHandler.remove(id + "");
					} catch (Exception e3) {
						Log.error(e3.toString());
					}
				}
				closeGlobalHandler(id);
			}
		} else {
			Log.info(String.format("在%s组里没有ID为%d的客户端", writeStr, id));
		}
		if (resend) {
			Behave errorBehave = new Behave(0);
			errorBehave.newInfo(id, fromid, 0, content);
		}
		return false;
	}




	protected static void closeGlobalHandler(int id) {
		MainHandler tem = globalHandler.get(id + "");
		if (tem.socket.isClosed()) {
			try {
				tem.socket.close();
				globalHandler.remove(id + "");
			} catch (Exception e) {
				Log.error(e.toString());
			}
		}
	}

	/**
	 * 取得命令行，可以是手机，也可以是板子
	 * 
	 * @param gameuid
	 * @param content
	 * @return
	 */
	public static boolean socketRead(String content, int id, int fromid,
			boolean tem) {
		Log.info(String.format("服务端收到的客户端%d报文为：%s", id, content));
		MessageControl.MessageControl(content, id, fromid, tem);
		return true;
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

	public static boolean moveSocketInGlobal(String temName, int globalName) {
		clearAndroid();
		temHandler.get(temName).tem = false;
		System.out.println("changeitnameto:" + globalName);
		temHandler.get(temName).id = globalName;
		temHandler.get(temName).fromid = globalName;
		globalHandler.remove(globalName + "");
		globalHandler.put(globalName + "", temHandler.get(temName));
		System.out.println(String.format("put in Global Haddle:%d now have:%d",
				globalName, globalHandler.size()));
		temHandler.remove(temName);
		System.out.println(String.format("remove Tem Haddle:%d", globalName));
		System.out.println("globalCount:" + globalHandler.size());
		System.out.println("temCount:" + temHandler.size());
		return true;
	}
}

