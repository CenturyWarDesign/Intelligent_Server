package com.centurywar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainHandler implements Runnable {
	private Socket socket;
	int id = 0;
	int client_id = 0;
	boolean enable = true;
	public MainHandler(Socket socket, int id, int client_id) {
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
				Main.socketRead(msg.trim().substring(0), client_id, id,this);
			}
		} catch (IOException e) {
			System.out.println("断开连接了");
			enable = false;
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
					Main.cleanSocket(id);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}