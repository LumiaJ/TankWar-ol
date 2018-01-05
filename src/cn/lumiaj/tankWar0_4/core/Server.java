package cn.lumiaj.tankWar0_4.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static int id =100;
	public static final int TCP_PORT = 2333;
	private List<CClient> clients;

	public Server() {
		clients = new ArrayList<CClient>();
	}

	@SuppressWarnings("resource")
	public void start() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (Exception e) {
		}
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				clients.add(new CClient(IP, udpPort));
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(id++);
				System.out.println(
						"a client connection :" + s.getInetAddress() + ":" + s.getPort() + "---udpPort:" + udpPort);
			} catch (IOException e) {
				e.printStackTrace();
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

	}

	@SuppressWarnings("unused")
	private class CClient {
		private String IP;
		private int udpPort;

		public String getIP() {
			return IP;
		}

		public void setIP(String iP) {
			IP = iP;
		}

		public int getUdpPort() {
			return udpPort;
		}

		public void setUdpPort(int udpPort) {
			this.udpPort = udpPort;
		}

		public CClient(String IP, int udpPort) {
			super();
			this.IP = IP;
			this.udpPort = udpPort;
		}

	}

	public static void main(String[] args) {
		new Server().start();
	}

}
