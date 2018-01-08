package cn.lumiaj.tankWar0_4.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static int id = 100;
	public static final int TCP_PORT = 12333;
	public static final int UDP_PORT = 12334;
	private List<CClient> clients;
	private boolean exist;

	@SuppressWarnings("resource")
	public void start() {
		new Thread(new UDPThread()).start();

		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (Exception e) {
		}
		while (true) {
			System.out.println("现在连接了"+clients.size()+"个客户端");
			Socket s = null;
			exist = false;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				for(int i=0;i<clients.size();i++) {
					if(clients.get(i).getIP().equals(IP) && clients.get(i).getUdpPort() == udpPort) {
						clients.remove(i);
						exist = true;
						break;
					}
				}
				if(!exist) {
					clients.add(new CClient(IP, udpPort));
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					dos.writeInt(id++);
					System.out.println(
							"a client connection :" + s.getInetAddress() + ":" + s.getPort() + "---udpPort:" + udpPort);
				}
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

	public Server() {
		this.clients = new ArrayList<CClient>();
	}

	public static void main(String[] args) {
		new Server().start();
	}

	private class CClient {
		private String IP;
		private int udpPort;

		public String getIP() {
			return IP;
		}

		public int getUdpPort() {
			return udpPort;
		}

		public CClient(String IP, int udpPort) {
			super();
			this.IP = IP;
			this.udpPort = udpPort;
		}

	}

	private class UDPThread implements Runnable {
		private byte[] bytes = new byte[1024];

		@Override
		public void run() {
			DatagramSocket ds = null;
			DatagramPacket dp = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("UDP thread start , port :" + UDP_PORT);
			while (ds != null) {
				dp = new DatagramPacket(bytes, bytes.length);
				try {
					ds.receive(dp);
					System.out.println("recevice a udp package");
					for (int i = 0; i < clients.size(); i++) {
						dp.setSocketAddress(new InetSocketAddress(clients.get(i).getIP(), clients.get(i).getUdpPort()));
						ds.send(dp);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
