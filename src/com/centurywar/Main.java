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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurywar.control.ArduinoControl;
import com.centurywar.control.ConstantCode;
import com.centurywar.control.ConstantControl;
import com.centurywar.control.MessageControl;

/**
 * 主程序函数和入口类
 * 
 * @author wanhin
 * 
 */
public class Main {
	// LOG4j----LOG固定格式，以后每个里面都要加上
	protected final static Log Log = LogFactory.getLog(Main.class);
	private int port = 8080;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 4000;
	// 这个参数是客户端面的地址，客户端正常登录后都会进入这个里面
	public static Map<String, MainHandler> globalHandler = new HashMap<String, MainHandler>();
	// 所有板子进来之后都放入这个里面
	public static Map<String, MainHandler> arduinoHandler = new HashMap<String, MainHandler>();
	// 客户端临时的地址，没有登录之前全在这里
	public static Map<String, MainHandler> temHandler = new HashMap<String, MainHandler>();
	// 临时表的一个递增的序列，没有实际意义
	private static int MaxTem = 1;
	public Main() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		Log.info("服务启动，等待请求！");
		System.out.println("waiting for");

		// 注册定期运行任务，每5秒进行一次，把send_log 里面的该要运行的任务放到缓存里面进行
		Timer timer = new Timer();
		timer.schedule(new TimingTask(), 3000, 50000);
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

				// 从当前连接的SOCKET里面读取第一行数据，arduino发送的是32位的串，android发送的字符串:arduino
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
					// 这里面是arduino登录
					System.out.println("Sec:" + sec);
					ArduinoModel arduinoModel = new ArduinoModel(sec);
					if (arduinoModel.id == 0) {
						System.out.println("不存在SEC为" + sec + "的板子");
						continue;
					}
					MainHandler temr = new MainHandler(socket,
							arduinoModel.id, false);
					ArduinoModel.updateIpAndPort(socket.getInetAddress()
							.toString(), socket.getPort(), arduinoModel.id);
					executorService.execute(temr);
					arduinoHandler.put(arduinoModel.id + "", temr);

					// 发送板子上线通知到客户端
					UsersModel.sendError(ConstantCode.USER_ARDUINO_LOGIN,
							arduinoModel.getUsersId());

					// 把所有的指令下发
					ArduinoControl.doAllCommand(arduinoModel.id);
					System.out.println(String.format(
							"put in Arduino Haddle:%d now have: %d ",
							arduinoModel.id, arduinoHandler.size()));
				} else {
					// 这里面是安卓登录，先把字放到临时的登录表里面
					MainHandler temr = new MainHandler(socket, MaxTem, true);
					executorService.execute(temr);
					clearTem();
					temHandler.put(MaxTem + "", temr);
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


	/**
	 * 清理没有用的临时连接
	 */
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

	/**
	 * 清理没有用的arduino连接
	 */
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

	/**
	 * 清理没有用的安卓连接
	 */
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


	

	/**
	 * 这个是向socket写入的主函数入口
	 * 
	 * @param id
	 *            要写入的id
	 * @param fromid
	 *            从哪个客户端写入的
	 * @param content
	 *            具体内容
	 * @param resend
	 *            是否需要重复发送，这里都不重复发送，我们用redis来处理这个事情了已经
	 * @param writetype
	 *            向三种对象里面写入数据，有三种不同的枚举值
	 * @return
	 */
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
				// 把socket给移除
				if (!temHandler.socket.isClosed()) {
					try {
						temHandler.socket.close();
						tempHandler.remove(id + "");
					} catch (Exception e3) {
						Log.error(e3.toString());
					}
				}
				Log.error(e.toString());
			}
		} else {
			Log.info(String.format("在%s组里没有ID为%d的客户端", writeStr, id));
		}
		// 是不是放入延迟重发表，现在已经不再使用就该方法
		if (resend) {
			Behave errorBehave = new Behave(0);
			errorBehave.newInfo(id, fromid, 0, content);
		}
		return false;
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

	/**
	 * 这个函数主要是把登录过的玩家的socket换个位置，放到global里面去，便于管理
	 * 
	 * @param temName
	 * @param globalName
	 * @return
	 */
	public static boolean moveSocketInGlobal(String temName, int globalName) {
		temHandler.get(temName).tem = false;
		System.out.println("changeitnameto:" + globalName);
		temHandler.get(temName).id = globalName;
		temHandler.get(temName).fromid = globalName;
		// 如果当前global里面有相应的用户记录，则让用户下线
		if (globalHandler.containsKey(globalName + "")) {
			UsersModel.sendError(ConstantCode.USER_MORE_THAN_ONE_ERROR,
					globalName);
		}
		globalHandler.remove(globalName + "");
		globalHandler.put(globalName + "", temHandler.get(temName));

		// 更新登录的用户IP及端口
		UsersModel.updateIpAndPort(temHandler.get(temName).socket
				.getInetAddress().toString(), temHandler.get(temName).socket
				.getPort(), globalName);

		System.out.println(String.format("put in Global Haddle:%d now have:%d",
				globalName, globalHandler.size()));
		temHandler.remove(temName);
		System.out.println(String.format("remove Tem Haddle:%d", globalName));
		System.out.println("globalCount:" + globalHandler.size());
		System.out.println("temCount:" + temHandler.size());
		return true;
	}
}
