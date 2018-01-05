package cn.lumiaj.tankWar0_4.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import cn.lumiaj.tankWar0_4.udpPackage.TankGoOnline;

public class NetClient {
	public static int UDP_PORT_START = 12121;
	private int udpPort;
	private Client client;
	DatagramSocket ds = null;

	public void connect(String ip, int port) {
		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			client.getPlayer().setId(dis.readInt());
		} catch (Exception e) {
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		TankGoOnline tgo = new TankGoOnline(client.getPlayer());
		tgo.send(ds, ip, Server.UDP_PORT);
	}
	

	public NetClient(Client client) {
		this.udpPort = UDP_PORT_START++;
		this.client = client;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
