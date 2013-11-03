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
		System.out.println("waiting for");
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
				if (sec.length() != 32) {
					// socket.close();
					// break;
				}
				User us = new User(sec);
				if (us.userName.equals("")) {
					System.out.println(sec + " has not init");
					socket.close();
					break;
				}
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				System.out.println("welcome " + us.userName);
				pw.println("welcome " + us.userName);
				executorService.execute(new Handler(socket, us.gameuid,
						us.client));
				globalSocket.put(us.gameuid + "", socket);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
	}

	public static boolean socketWrite(int gameuid, String content) {
		if (globalSocket.containsKey(gameuid + "")) {
			Socket socket = globalSocket.get(gameuid + "");
			try {
				OutputStream socketOut = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(socketOut, true);
				// Thread.sleep(5000);
				// pw.println(content);
				// Thread.sleep(5000);
				// pw.println(content);
				// Thread.sleep(5000);
				pw.println(content);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean socketRead(int gameuid, String content) {
		System.out.println(gameuid + " get " + content.trim());
		return true;
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

}

class Handler implements Runnable {
	private Socket socket;
	int id = 0;
	int client_id = 0;

	public Handler(Socket socket, int id, int client_id) {
		this.socket = socket;
		this.id = id;
		this.client_id = client_id;
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
				Main.socketRead(id, msg.trim().substring(0));
				if (this.client_id > 0) {
					Main.socketWrite(client_id, msg);
					System.out.println(client_id + " write " + msg);
				}
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