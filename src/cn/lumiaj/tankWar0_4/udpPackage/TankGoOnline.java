package cn.lumiaj.tankWar0_4.udpPackage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import cn.lumiaj.tankWar0_4.bean.Player;
import cn.lumiaj.utils.Direction;

public class TankGoOnline implements UDPPackage {
	private Player player;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Direction direction = Direction.values()[dis.readInt()];
			System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" + direction);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void send(DatagramSocket ds, String ip, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
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

	public TankGoOnline(Player player) {
		this.player = player;
	}

	public TankGoOnline() {
	}

}
