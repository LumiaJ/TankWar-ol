package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.core.Client;

public class TankHPChangeMsg implements UDPPackage{
	private int msgType, id, HP;
	private Client client;
	
	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(HP);
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
			HP = dis.readInt();
			if (id == client.getPlayer().getId()) {
				client.getPlayer().setHP(HP);
				return;
			}
			for(int i=0;i<client.getOther().size();i++) {
				if(id == client.getOther().get(i).getId()) {
					client.getOther().get(i).setHP(HP);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TankHPChangeMsg(int HP, int id) {
		this();
		this.id = id;
		this.HP = HP;
	}
	
	public TankHPChangeMsg(Client client) {
		this();
		this.client = client;
	}
	
	public TankHPChangeMsg() {
		this.msgType = UDPPackage.TANK_HP_CHANGE_MSG;
	}
	
	
}
