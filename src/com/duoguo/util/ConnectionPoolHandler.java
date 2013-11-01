package com.duoguo.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shyboy(chao.shen@duoguo.cn)
 * 
 */
public class ConnectionPoolHandler implements Runnable {
	private Socket socket;
	@SuppressWarnings("unchecked")
	private static List pool = new LinkedList();

	/**
	 * 处理连接
	 */
	public void handleConnection() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String line = in.readLine();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					line));
			String value = null;
			while ((value = bufferedReader.readLine()) != null) {
				out.println(value);
			}
			bufferedReader.close();
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void processRequest(Socket socket) {
		synchronized (pool) {
			pool.add(pool.size(), socket);
			pool.notifyAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			synchronized (pool) {
				while (pool.isEmpty()) {
					try {
						pool.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				socket = (Socket) pool.remove(0);
			}
			handleConnection();
		}
	}
}

/**
 *
 */
