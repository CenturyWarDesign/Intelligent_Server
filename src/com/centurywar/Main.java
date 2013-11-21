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
	public static Map<String, Socket> globalSocket = new HashMap<String, Socket>();
	public static Map<String,Socket> temSocket= new HashMap<String, Socket>();
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
				executorService.execute(new MainHandler(socket, MaxTem, MaxTem));
				temSocket.put(MaxTem + "", socket);
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
	public static boolean socketWrite(int gameuid, int fromgameuid,String content,boolean resend) {
		if (gameuid <= 0) {
			return false;
		}
		if (globalSocket.containsKey(gameuid + "")) {
			Socket socket = globalSocket.get(gameuid + "");
			try {
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				pw.println(content);
				
				//存入缓存
				String key = gameuid+content;
				Integer time = new Integer((int) (System.currentTimeMillis()/1000));
				Redis.set(key,time.toString());
				System.out.println("[send to client]" + content);
				return true;
			} catch (Exception e) {
				// 记录失败的程序
				e.printStackTrace();
				// 把socket给移除
				cleanSocket(gameuid);
				System.out.println(String.format("[send to client %d error]",
						gameuid));
			}
		} else {
			System.out.println("No gameuid in globalSockets");
		}
		if (!resend) {
			Behave errorBehave = new Behave(0);
			errorBehave.newInfo(gameuid, fromgameuid, 0, content);
		}
		return false;
	}

	public static void cleanSocket(int id) {
		Socket socket = globalSocket.get(id + "");
		try {
			socket.close();
			socket = null;
			globalSocket.remove(id + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得命令行，可以是手机，也可以是板子
	 * @param gameuid
	 * @param content
	 * @return
	 */
	public static boolean socketRead(String content, int gameuid,
			int fromgameuid, MainHandler handler) {
		System.out.println("服务端收到的报文为："+content);
		MessageControl.MessageControl(content, gameuid, fromgameuid,handler);
		return true;
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

}

