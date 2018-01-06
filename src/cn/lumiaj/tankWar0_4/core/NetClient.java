package cn.lumiaj.tankWar0_4.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import cn.lumiaj.tankWar0_4.udpPackage.TankGoOnline;
import cn.lumiaj.tankWar0_4.udpPackage.TankMoveMsg;
import cn.lumiaj.tankWar0_4.udpPackage.UDPPackage;

public class NetClient {
	public static int UDP_PORT_START = 12121;
	private int udpPort;
	private Client client;
	DatagramSocket ds = null;

	public void connect(String ip, int port) {
		Socket s = null;
		try {
			s = new Socket(ip, port);
			// 连接成功后打开UDP线程
			new Thread(new UDPRecThread()).start();

			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			client.getPlayer().setId(dis.readInt());
		} catch (Exception e) {
			System.err.println("服务器未连接");
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
		TankGoOnline tgo = new TankGoOnline(client);
		send(tgo);
	}

	public void send(UDPPackage data) {
		data.send(ds, "localhost", Server.UDP_PORT);
	}

	public DatagramSocket getDs() {
		return ds;
	}

	public NetClient(Client client) {
		this.udpPort = (int) (Math.random() * 1000 + 10000);
		this.client = client;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private class UDPRecThread implements Runnable {
		private byte[] bytes = new byte[1024];

		@Override
		public void run() {
			DatagramPacket dp = null;
			while (ds != null) {
				dp = new DatagramPacket(bytes, bytes.length);
				try {
					ds.receive(dp);
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes, 0, dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			UDPPackage data = null;
			try {
				switch(dis.readInt()) {
				case UDPPackage.TANK_ONLINE_MSG:
					data = new TankGoOnline(NetClient.this.client);
					break;
				case UDPPackage.TANK_MOVE_MSG:
					data = new TankMoveMsg(NetClient.this.client);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(data != null)
				data.parse(dis);
		}
	}

}
