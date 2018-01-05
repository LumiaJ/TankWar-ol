package cn.lumiaj.tankWar0_4.bean;

import java.awt.Color;
import java.awt.Graphics;

import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.tankWar0_4.core.Explode;
import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Direction;

public class Robot extends Tank {
	private Tank player;
	private Explode e;
//	private RobotMode rm;

	public void redirection() {
		int x_d = x - player.x, y_d = y - player.y;
		if (Math.abs(x_d * 1.0 / y_d) < 0.1 || Math.abs(y_d * 1.0 / x_d) < 0.1) {
			if (y_d > x_d && Math.abs(y_d) > Math.abs(x_d))
				direction = Direction.U;
			if (y_d > x_d && Math.abs(y_d) < Math.abs(x_d))
				direction = Direction.R;
			if (y_d < x_d && Math.abs(y_d) > Math.abs(x_d))
				direction = Direction.D;
			if (y_d < x_d && Math.abs(y_d) < Math.abs(x_d))
				direction = Direction.L;
		} else if (x_d > 0 && y_d > 0) { // 第一象限
			if (y_d * 0.4 > x_d)
				direction = Direction.L;
			else if (x_d * 0.4 > y_d)
				direction = Direction.U;
			else
				direction = Direction.UL;
		} else if (x_d < 0 && y_d > 0) { // 第二象限
			if (y_d * 0.4 > -x_d)
				direction = Direction.R;
			else if (-x_d * 0.4 > y_d)
				direction = Direction.U;
			else
				direction = Direction.UR;
		} else if (x_d < 0 && y_d < 0) { // 第三象限
			if (y_d * 0.4 < x_d)
				direction = Direction.R;
			else if (x_d * 0.4 < y_d)
				direction = Direction.D;
			else
				direction = Direction.DR;
		} else if (x_d > 0 && y_d < 0) { // 第四象限
			if (-y_d * 0.4 > x_d)
				direction = Direction.L;
			else if (x_d * 0.4 > -y_d)
				direction = Direction.D;
			else
				direction = Direction.DL;
		}
		ptDirection = direction;
	}

	public void paint(Graphics g) {
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint(g);
		}
		if (!isAlive) {
			if(e == null) e = new Explode(x, y);
			if(!e.paint(g)) {
				if (bullets.isEmpty()) {
					client.getRobots().remove(this);
				}
			}
			return;
		}
		redirection();
		move();
		Color c = g.getColor();
		g.setColor(Constants.ROBOT_COLOR);
		g.fillOval(x, y, Constants.TANK_SIZE, Constants.TANK_SIZE);
		paintPT(g, ptDirection);
		g.setColor(c);
	}

	public Robot(int x, int y, Client client) {
		this.x = x;
		this.y = y;
		this.client = client;
		this.player = client.getPlayer();
		this.speed = Constants.ROBOT_SPEED;
		this.direction = Direction.S;
		this.ptDirection = Direction.U;
		this.bulletColor = Constants.ROBOT_BULLET_COLOR;
		this.isAlive = true;
//		new Thread(new RobotMode(this)).start();
//		this.rm = new RobotMode(this);
	}

	@Override
	public void boom() {
		isAlive = false;
	}

}
