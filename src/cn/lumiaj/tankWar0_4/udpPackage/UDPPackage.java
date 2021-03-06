package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface UDPPackage {
	public static final int TANK_ONLINE_MSG = 1;
	public static final int TANK_MOVE_MSG = 2;
	public static final int TANK_OFFLINE_MSG = 3;
	public static final int TANK_SHUT_MSG = 4;
	public static final int TANK_SPEED_CHANGE_MSG = 5;
	public static final int TANK_HP_CHANGE_MSG = 6;
	public static final int BULLET_DIE_MSG = 7;
	public static final int GAME_OVER_MSG = 8;
	
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
