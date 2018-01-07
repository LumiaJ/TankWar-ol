package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.core.Client;

public class TankSpeedChangeMsg implements UDPPackage{
	private int msgType, speed;
	private Client client;
	
	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(client.getPlayer().getId());
			dos.writeInt(speed);
			byte[] bytes = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(ip, udpPort));
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (id == client.getPlayer().getId())
				return;
			speed = dis.readInt();
			for(int i=0;i<client.getOther().size();i++) {
				if(id == client.getOther().get(i).getId()) {
					client.getOther().get(i).setSpeed(speed);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TankSpeedChangeMsg(int speed, Client client) {
		this(client);
		this.speed = speed;
	}
	
	public TankSpeedChangeMsg(Client client) {
		this();
		this.client = client;
	}
	
	public TankSpeedChangeMsg() {
		this.msgType = UDPPackage.TANK_SPEED_CHANGE_MSG;
	}
}
