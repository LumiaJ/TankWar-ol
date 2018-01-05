package cn.lumiaj.tankWar0_4.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetClient {
	public static int UDP_PORT_START = 12121;
	private int udpPort;

	public void connect(String ip, int port) {
		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
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

	public NetClient() {
		udpPort = UDP_PORT_START++;
	}

}
