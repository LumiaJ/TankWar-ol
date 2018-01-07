package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.bean.Bullet;
import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.utils.Direction;

public class TankShutMsg implements UDPPackage {
	private int msgType;
	private Client client;
	private Bullet b;


	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(client.getPlayer().getId());
			dos.writeInt(b.getX());
			dos.writeInt(b.getY());
			dos.writeInt(b.getDirection().ordinal());
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
			int x = dis.readInt();
			int y = dis.readInt();
			Direction direction = Direction.values()[dis.readInt()];
			for(int i=0;i<client.getOther().size();i++) {
				if(id == client.getOther().get(i).getId()) {
					b = new Bullet(x, y, client.getOther().get(i));
					b.setDirection(direction);
					client.getOther().get(i).shut(b);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TankShutMsg(Bullet b, Client client) {
		this();
		this.b = b;
		this.client = client;
	}

	public TankShutMsg(Client client) {
		this();
		this.client = client;
	}
	
	public TankShutMsg() {
		this.msgType = UDPPackage.TANK_SHUT_MSG;
	}
}
