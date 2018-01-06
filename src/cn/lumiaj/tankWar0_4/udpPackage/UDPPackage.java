package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface UDPPackage {
	/**
	 * 向指定的服务器端发送数据
	 * @param ds UDP协议的socket
	 * @param ip 服务器的IP
	 * @param udpPort 服务器的UDP端口号
	 */
	void send(DatagramSocket ds, String ip, int udpPort);
	
	/**
	 * 分析接收到的DatagramPackage
	 * @param dis 包含DatagramPackage的DataInputStream
	 */
	void parse(DataInputStream dis);
		
}
