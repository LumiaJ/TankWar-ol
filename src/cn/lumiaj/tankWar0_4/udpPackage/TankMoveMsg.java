package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.utils.Direction;

public class TankMoveMsg implements UDPPackage{
	private int id, msgType, x, y;
	private Direction direction;
	private Client client;

	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(direction.ordinal());
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
			if(id == client.getPlayer().getId()) 
				return;
			this.x = dis.readInt();
			this.y = dis.readInt();
			Direction direction = Direction.values()[dis.readInt()];
			for(int i=0; i<client.getOther().size();i++) {
				if(client.getOther().get(i).getId() == id) {
					client.getOther().get(i).setPosition(x, y);
					client.getOther().get(i).setDirection(direction);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TankMoveMsg(int id, int x, int y, Direction direction) {
		this();
		this.id = id;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public TankMoveMsg(Client client) {
		this.client = client;
	}
	
	public TankMoveMsg() {
		this.msgType = UDPPackage.TANK_MOVE_MSG;
	}
	
}
