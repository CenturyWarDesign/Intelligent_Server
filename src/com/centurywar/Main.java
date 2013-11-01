package com.centurywar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private int port = 8080;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 10;
	private static Map<String, Socket> globalSocket = new HashMap<String, Socket>();

	public Main() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("连接服务器");
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
				if (sec.length() != 32) {
					break;
				}
				User us = new User(sec);
				if (us.userName.equals("")) {
					System.out.println(sec + " has not init");
					break;
				}
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				pw.println("welcome " + us.userName);
				executorService.execute(new Handler(socket, us.gameuid));
				globalSocket.put(us.gameuid + "", socket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean socketWrite(int gameuid, String content) {
		if (globalSocket.containsKey(gameuid + "")) {
			Socket socket = globalSocket.get(gameuid + "");
			try {
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				pw.println(content);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean socketRead(int gameuid, String content) {
		System.out.println(gameuid + "get" + content);
		return true;
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

}

class Handler implements Runnable {
	private Socket socket;
	int id = 0;

	public Handler(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
	}

	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
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
				Main.socketRead(id, msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}