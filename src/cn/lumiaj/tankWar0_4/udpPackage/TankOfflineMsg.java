package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.core.Client;

public class TankOfflineMsg implements UDPPackage{
	private int id, msgType;
	private Client client;

	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
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
			for(int i=0;i<client.getOther().size();i++) {
				if(client.getOther().get(i).getId() == id) {
					client.getOther().remove(i);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TankOfflineMsg(int id, Client client) {
		this();
		this.id = id;
		this.client = client;
	}
	
	public TankOfflineMsg(Client client) {
		this();
		this.client = client;
	}
	
	public TankOfflineMsg() {
		this.msgType = UDPPackage.TANK_OFFLINE_MSG;
	}

}
