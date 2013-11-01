package com.duoguo.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author shyboy(chao.shen@duoguo.cn)
 * 
 */
public class Client {
	private String hostIp;// 客户端主机IP
	private int hostPort;// 客户端主机端口号
	private BufferedReader in;// 客户端输入流
	private PrintWriter out;// 客户端输出流

	/**
	 * 构造方法
	 * 
	 * @param hostip
	 *            ：IP地址
	 * @param hostPort
	 *            ：端口号
	 */
	public Client(String hostip, int hostPort) {
		this.hostIp = hostip;
		this.hostPort = hostPort;
	}

	/**
	 * 建立连接
	 */
	public void setUpConnection() {
		try {
			Socket client = new Socket(hostIp, hostPort);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));// 客户端输入流
			out = new PrintWriter(client.getOutputStream());// 客户端输出流
		} catch (UnknownHostException e) {
			System.out.println("找不到相应的主机！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("抛出相应的流异常信息");
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件
	 * 
	 * @param fileName
	 *            ：文件名称
	 * @return String
	 */
	public String getFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		out.println(fileName);
		out.flush();
		String line = null;
		try {
			System.out.println("客户端连接成功！");
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			System.out.println("文件读入失败！");
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 断开连接
	 */
	public void tearDownConnection() {
		try {
			out.close();// 关闭输出流
			in.close();// 关闭输入流
		} catch (IOException e) {
			System.out.println("断开连接失败！");
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 9999);
		client.setUpConnection();
		String fileContent = client.getFile("D:\\hello.txt");
		System.out.println("文件内容为：" + "\n" + fileContent);
		client.tearDownConnection();
	}
}
