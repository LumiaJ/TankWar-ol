package cn.lumiaj.tankWar0_4.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import cn.lumiaj.tankWar0_4.udpPackage.OfflineMsg;
import cn.lumiaj.tankWar0_4.udpPackage.TankGoOnline;
import cn.lumiaj.tankWar0_4.udpPackage.TankMoveMsg;
import cn.lumiaj.tankWar0_4.udpPackage.UDPPackage;

public class NetClient {
	private int udpPort;
	private Client client;
	private DatagramSocket ds = null;
	private boolean existDs;

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public boolean connect(String ip, int port) {
		if(!existDs) {
			try {
				ds = new DatagramSocket(udpPort);
				existDs = true;
			} catch (SocketException e) {
				e.printStackTrace();
				existDs =false;
			}
		}
		
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
			return false;
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
		return true;
	}

	public void send(UDPPackage data) {
		data.send(ds, "localhost", Server.UDP_PORT);
	}

	public DatagramSocket getDs() {
		return ds;
	}

	public NetClient(Client client) {
		// this.udpPort = (int) (Math.random() * 1000 + 10000);
		this.client = client;
		this.existDs = false;
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
				switch (dis.readInt()) {
				case UDPPackage.TANK_ONLINE_MSG:
					data = new TankGoOnline(NetClient.this.client);
					break;
				case UDPPackage.TANK_MOVE_MSG:
					data = new TankMoveMsg(NetClient.this.client);
					break;
				case UDPPackage.OFFLINE_MSG:
					data = new OfflineMsg(NetClient.this.client);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (data != null)
				data.parse(dis);
		}
	}

}
