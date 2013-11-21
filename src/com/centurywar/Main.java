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

import com.centurywar.control.MessageControl;

public class Main {
	private int port = 8080;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 10;
	public static Map<String, MainHandler> globalHandler = new HashMap<String, MainHandler>();
	public static Map<String, MainHandler> temHandler = new HashMap<String, MainHandler>();
	private static int MaxTem=0;
	
	public Main() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("waiting for");
		Timer timer = new Timer();
		timer.schedule(new TimingTask(), 6000, 20000);
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("First get it " + socket.getInetAddress()
						+ ":" + socket.getPort());
				String sec = null;
				InputStream socketIn = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						socketIn));
				sec = br.readLine();
				System.out.println("Sec:" + sec);
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 设置日期格式
				System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
				MainHandler temr = new MainHandler(socket, MaxTem);
				executorService.execute(temr);
				temHandler.put(MaxTem + "", temr);
				MaxTem++;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * 输出。
	 * 
	 * @param gameuid
	 * @param fromgameuid
	 * @param content
	 * @param resend
	 * @return
	 */
	public static boolean socketWrite(int id, int fromid, String content,
			boolean resend) {
		if (id <= 0) {
			return false;
		}
		if (globalHandler.containsKey(id + "")) {
			MainHandler temHandler = globalHandler.get(id);
			try {
				OutputStream socketOut = temHandler.socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				pw.println(content);

				// 存入缓存
				String key = id + content;
				Integer time = new Integer(
						(int) (System.currentTimeMillis() / 1000));
				Redis.set(key, time.toString());
				System.out.println("[send to client]" + content);
				return true;
			} catch (Exception e) {
				// 记录失败的程序
				e.printStackTrace();
				// 把socket给移除
				cleanSocket(id);
				System.out.println(String.format("[send to client %d error]",
						id));
			}
		} else {
			System.out.println("No gameuid in globalSockets");
		}
		if (!resend) {
			Behave errorBehave = new Behave(0);
			errorBehave.newInfo(id, fromid, 0, content);
		}
		return false;
	}

	public static void cleanSocket(int id) {
		Socket socket = globalHandler.get(id + "").socket;
		try {
			socket.close();
			socket = null;
			globalHandler.remove(id + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得命令行，可以是手机，也可以是板子
	 * 
	 * @param gameuid
	 * @param content
	 * @return
	 */
	public static boolean socketRead(String content, int id) {
		System.out.println("服务端收到的报文为：" + content);
		MessageControl.MessageControl(content, id, id);
		return true;
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

}

