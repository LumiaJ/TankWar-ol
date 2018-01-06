package cn.lumiaj.tankWar0_4.core;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import cn.lumiaj.tankWar0_4.bean.Player;
import cn.lumiaj.tankWar0_4.udpPackage.OfflineMsg;
import cn.lumiaj.utils.Constants;

@SuppressWarnings("all")
public class Client extends Frame {
	private Image offScreenImage;
	private Player player;
	private List<Player> other;
	private NetClient nc;
	private ConnDialog dialog;
	private boolean online;

	public boolean isOnline() {
		return online;
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * 继承update方法，实现双缓冲解决闪现问题
	 */
	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(Constants.BOUND_WIDTH, Constants.BOUND_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, Constants.BOUND_WIDTH, Constants.BOUND_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		g.drawString("我方子弹数量:" + player.getBullets().size(), 30, 100);
		player.paint(g);
		for (int i = 0; i < other.size(); i++) {
			other.get(i).paint(g);
		}
	}

	public void initPlayer() {
		int x = (int) (Math.random() * (Constants.BOUND_WIDTH - Constants.TANK_SIZE));
		int y = (int) (Math.random() * (Constants.BOUND_HEIGHT - Constants.TANK_SIZE * 2) + Constants.TANK_SIZE);
		this.player = new Player(x, y, this);
	}

	/**
	 * 初始化客户端
	 */
	public void launchFrame(int count) {
		this.setBounds(Constants.BOUND_X, Constants.BOUND_Y, Constants.BOUND_WIDTH, Constants.BOUND_HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Tank War");
		this.setBackground(Color.GRAY);
		this.nc = new NetClient(this);
		// 添加窗口的关闭
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (online) {
					Client.this.nc.send(new OfflineMsg(player.getId(), Client.this));
					nc.connect(dialog.tfIP.getText(), Integer.parseInt(dialog.tfPort.getText()));
				}
				System.exit(0);
			}
		});
		// 添加键盘控制
		this.addKeyListener(new KeyMonitor());
		// 添加坦克
		initPlayer();
		other = new ArrayList<Player>();
		// 线程开始
		this.dialog = new ConnDialog();
		this.online = false;
		new Thread(new PaintThread()).start();
	}

	public NetClient getNc() {
		return nc;
	}

	public List<Player> getOther() {
		return other;
	}

	/**
	 * 键盘的监听类
	 * 
	 * @author LumiaJ
	 *
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_P)
				dialog.setVisible(true);
			else
				player.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}
	}

	private class PaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					repaint();
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ConnDialog extends Dialog {
		private Button b;
		private TextField tfIP;
		private TextField tfPort;
		private TextField tfUDPPort;

		public ConnDialog() {
			super(Client.this, true);
			init();
		}

		private void init() {
			b = new Button("OK!");
			tfIP = new TextField("localhost", 12);
			tfPort = new TextField(2333 + "", 4);
			tfUDPPort = new TextField("" + 10000, 4);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfUDPPort);
			this.setLocation(Constants.BOUND_X, Constants.BOUND_Y);
			this.add(b);
			this.pack();
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}

			});
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String ip = tfIP.getText();
					int port = Integer.parseInt(tfPort.getText());
					int udpPort = Integer.parseInt(tfUDPPort.getText());
					nc.setUdpPort(udpPort);
					Client.this.online = nc.connect(ip, port);
					setVisible(false);
				}
			});
		}

	}

	public static void main(String[] args) {
		new Client().launchFrame(2);
	}
}
