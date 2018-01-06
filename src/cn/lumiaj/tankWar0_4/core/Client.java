package cn.lumiaj.tankWar0_4.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import cn.lumiaj.tankWar0_4.bean.Player;
import cn.lumiaj.utils.Constants;

@SuppressWarnings("all")
public class Client extends Frame {
	private Image offScreenImage;
	private Player player;
	private List<Player> other;
	private boolean isOver;
	private NetClient nc;

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
				System.exit(0);
			}
		});
		// 添加键盘控制
		this.addKeyListener(new KeyMonitor());
		// 添加坦克
		initPlayer();
		other = new ArrayList<Player>();
		// 线程开始
		isOver = false;
		new Thread(new PaintThread()).start();
		// 连接到服务器
		nc.connect("localhost", Server.TCP_PORT);
	}

	public NetClient getNc() {
		return nc;
	}

	public List<Player> getOther() {
		return other;
	}

	public void gameOver() {
		isOver = true;
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
			while (!isOver) {
				try {
					repaint();
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new Client().launchFrame(2);
	}
}
