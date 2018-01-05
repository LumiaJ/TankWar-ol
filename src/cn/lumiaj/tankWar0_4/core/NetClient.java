package cn.lumiaj.tankWar0_4.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetClient {
	public static int UDP_PORT_START = 12121;
	private int udpPort;
	private Client client;

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
	}

	public NetClient(Client client) {
		udpPort = UDP_PORT_START++;
		this.client = client;
	}

}
