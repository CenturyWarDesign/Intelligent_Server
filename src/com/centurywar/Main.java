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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private int port = 8080;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 10;
	private Map<String, Socket> globalSocket = null;
	private static List pool = new LinkedList();

	public Main() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("连接服务器");
		globalSocket = new HashMap<String, Socket>();
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
				System.out.println("welcome:" + us.getUserInfo().userName);

				// executorService.execute(new Handler(socket));
				// globalSocket.put("tem1", socket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Main().service();
	}

}

class Handler implements Runnable {
	private Socket socket;
	int i = 0;

	public Handler(Socket socket) {
		this.socket = socket;
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

	// public int getvip(int i) {
	// try {
	// ResultSet rs = JDBC.query("select * from user_vip limit " + i
	// + ",1");
	// // return 0;
	// while (rs.next() && rs != null) {
	// return rs.getInt("gameuid");
	// }
	// } catch (Exception e) {
	// System.out.println("[hero]" + e);
	// }
	// return 0;
	// }

	public void run() {
		try {
			System.out.println("New connection accepted "
					+ socket.getInetAddress() + ":" + socket.getPort());
			// BufferedReader br = getReader(socket);
			PrintWriter pw = getWriter(socket);
			// String msg = null;
			while (true) {
				// pw.println(getvip(i++) + "");
				System.out.println("1000-server");
				pw.println("1000");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					return;
				}
			}
			// while((msg=br.readLine())!=null){
			// System.out.println(msg);
			// pw.println(echo(msg));
			// if(msg.equals("bye"))
			// break;
			// }
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