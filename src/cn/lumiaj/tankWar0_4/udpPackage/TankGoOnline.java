package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.bean.Player;
import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.utils.Direction;

public class TankGoOnline implements UDPPackage {
	private Player player;
	private Client client;
	private int msgType;

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
				if(id == client.getOther().get(i).getId()) 
					return;
			}
			client.getOther().add(new Player(id, x, y, direction, client));
			client.getNc().send(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(player.getId());
			dos.writeInt(player.getX());
			dos.writeInt(player.getY());
			dos.writeInt(player.getDirection().ordinal());
			byte[] bytes = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(ip, udpPort));
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TankGoOnline(Client client) {
		this();
		this.client = client;
		this.player = this.client.getPlayer();
	}

	public TankGoOnline() {
		this.msgType = UDPPackage.TANK_ONLINE_MSG;
	}

}
