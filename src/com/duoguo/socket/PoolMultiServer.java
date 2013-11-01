package com.duoguo.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.duoguo.util.ConnectionPoolHandler;

/**
 * @author shyboy(chao.shen@duoguo.cn)
 * 
 */
public class PoolMultiServer {
	private int maxConnections;// 最大连接数
	private int listenerPort;// 监听端口号
	private ServerSocket serverSocket;

	/**
	 * 构造方法
	 * 
	 * @param maxConnections
	 *            ：最大连接数
	 * @param listenerPort
	 *            ：监听端口号
	 */
	public PoolMultiServer(int maxConnections, int listenerPort) {
		this.maxConnections = maxConnections;
		this.listenerPort = listenerPort;
	}

	/**
	 * 接受连接
	 */
	public void acceptConnection() {
		try {
			serverSocket = new ServerSocket(listenerPort, maxConnections);
			while (true) {
				Socket socket = serverSocket.accept();
				handleConnection(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理连接
	 * 
	 * @param socket
	 *            ：套接字
	 */
	public void handleConnection(Socket socket) {
		ConnectionPoolHandler.processRequest(socket);
	}

	public void setUpHandlers() {
		for (int i = 0; i < maxConnections; i++) {
			ConnectionPoolHandler connectionPoolHandler = new ConnectionPoolHandler();
			new Thread(connectionPoolHandler, "处理器" + i).start();
			System.out.println("1000-server");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PoolMultiServer poolMultiServer = new PoolMultiServer(5, 8080);
		poolMultiServer.setUpHandlers();
		poolMultiServer.acceptConnection();
	}
}

/**
 *
 */
