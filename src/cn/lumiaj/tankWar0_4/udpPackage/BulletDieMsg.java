package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.core.Client;

public class BulletDieMsg implements UDPPackage {
	private int msgType, id, num;
	private Client client;
	
	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(num);
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
			id = dis.readInt();
			num = dis.readInt();
			if(id == client.getPlayer().getId()) {
				client.getPlayer().getBullets().remove(num);
			}
			for(int i=0; i<client.getOther().size();i++) {
				if(client.getOther().get(i).getId() == id) {
					client.getOther().get(i).getBullets().remove(num);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BulletDieMsg(int id, int num) {
		this();
		this.id = id;
		this.num = num;
	}
	
	public BulletDieMsg(Client client) {
		this();
		this.client = client;
	}
	
	public BulletDieMsg() {
		this.msgType = UDPPackage.BULLET_DIE_MSG;
	}
	
}
