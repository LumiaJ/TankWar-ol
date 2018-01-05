package cn.lumiaj.tankWar0_4.core;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	public static final int TCP_PORT = 2333;
	private List<CClient> clients;

	public Server() {
		clients = new ArrayList<CClient>();
	}

	public void start() {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				clients.add(new CClient(IP, udpPort));
				s.close();
//				System.out.println("a client connection :" + s.getInetAddress() + ":" + s.getPort());
			}
		} catch (Exception e) {
		}
	}

	private class CClient {
		private String IP;
		private int udpPort;

		public CClient(String IP, int udpPort) {
			super();
			IP = IP;
			this.udpPort = udpPort;
		}

	}

	public static void main(String[] args) {
		new Server().start();
	}

}
