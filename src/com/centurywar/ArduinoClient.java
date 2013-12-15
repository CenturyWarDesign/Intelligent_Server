package com.centurywar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.centurywar.control.BaseControl;

/**
 * 测试程序，测试板子性能，和返回值的工具
 * 
 * @author wanhin
 * 
 */
public class ArduinoClient {

	// private static final String HOST = "42.121.123.185";
	private static final String HOST = "192.168.1.31";
	private static final int PORT = 8080;
	// private static final int PORT = 8686;
	private PrintWriter pw;
	public Socket socket;
	public boolean initSocket = false;
	private ExecutorService executorService;
	private final int POOL_SIZE = 10000;
	public String sendTem = "";
	public static int temid = 1;

	public ArduinoClient() throws IOException {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
	}

	public void service() {
		try {
			socket = new Socket(HOST, PORT);
			OutputStream socketOut = socket.getOutputStream();
			pw = new PrintWriter(socketOut, true);
			Random r = new Random();
			String write = BaseControl.EncoderPwdByMd5(String.format(
					"Intelligent%d%d",
					temid, r.nextInt()));
			System.out.println(write);
			pw.println(write);
			executorService.execute(new Handler(socket));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean socketRead(String content) throws Exception {
		System.out.println("[get from server]" + content);
		return true;
	}

	class Handler implements Runnable {
		private Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		private BufferedReader getReader(Socket socket) throws IOException {
			InputStream socketIn = socket.getInputStream();
			return new BufferedReader(new InputStreamReader(socketIn));
		}

		public String echo(String msg) {
			return "echo:" + msg;
		}

		public void run() {
			try {
				BufferedReader br = getReader(socket);
				String msg = null;
				while ((msg = br.readLine()) != null) {
					try {
						ArduinoClient.socketRead(msg);
					} catch (Exception e) {

					}
				}
			} catch (IOException e) {
				System.out.println("断开连接了");
				e.printStackTrace();
			} finally {
				try {
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		for (int i = 0; i < 2000; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						new ArduinoClient().service();
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}.start();
		}
		while (true) {

		}
	}

}
