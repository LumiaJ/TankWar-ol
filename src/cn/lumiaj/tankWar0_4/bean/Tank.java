package cn.lumiaj.tankWar0_4.bean;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Direction;

public abstract class Tank {
	protected int x, y, speed;
	protected List<Bullet> bullets = new ArrayList<Bullet>();
	protected Direction direction = Direction.S;
	protected Color bulletColor;
	protected Direction ptDirection;
	protected Client client;
	protected boolean isAlive;

	public abstract void boom();

	// 画出炮筒
	public void paintPT(Graphics g, Direction dir) {
		g.setColor(Color.ORANGE);
		switch (dir) {
		case U:
			g.drawLine((int) (x + 0.5 * Constants.TANK_SIZE), (int) (y + 0.5 * Constants.TANK_SIZE),
					(int) (x + 0.5 * Constants.TANK_SIZE), y - 10);
			break;
		case D:
			g.drawLine((int) (x + 0.5 * Constants.TANK_SIZE), (int) (y + 0.5 * Constants.TANK_SIZE),
					(int) (x + 0.5 * Constants.TANK_SIZE), y + Constants.TANK_SIZE + 10);
			break;
		case L:
			g.drawLine(x - 10, (int) (y + 0.5 * Constants.TANK_SIZE), (int) (x + 0.5 * Constants.TANK_SIZE),
					(int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case R:
			g.drawLine(x + Constants.TANK_SIZE + 10, (int) (y + 0.5 * Constants.TANK_SIZE),
					(int) (x + 0.5 * Constants.TANK_SIZE), (int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case UR:
			g.drawLine(x + Constants.TANK_SIZE, y, (int) (x + 0.5 * Constants.TANK_SIZE),
					(int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case UL:
			g.drawLine(x, y, (int) (x + 0.5 * Constants.TANK_SIZE), (int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case DR:
			g.drawLine(x + Constants.TANK_SIZE, y + Constants.TANK_SIZE, (int) (x + 0.5 * Constants.TANK_SIZE),
					(int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case DL:
			g.drawLine(x, y + Constants.TANK_SIZE, (int) (x + 0.5 * Constants.TANK_SIZE),
					(int) (y + 0.5 * Constants.TANK_SIZE));
			break;
		case S:
			break;
		}
	}

	public void move() {
		switch (direction) {
		case S:
			break;
		case U:
			y -= speed;
			break;
		case D:
			y += speed;
			break;
		case L:
			x -= speed;
			break;
		case R:
			x += speed;
			break;
		case UR:
			y -= speed;
			x += speed;
			break;
		case UL:
			y -= speed;
			x -= speed;
			break;
		case DR:
			y += speed;
			x += speed;
			break;
		case DL:
			y += speed;
			x -= speed;
			break;
		}
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + Constants.TANK_SIZE > Constants.BOUND_WIDTH)
			x = Constants.BOUND_WIDTH - Constants.TANK_SIZE;
		if (y + Constants.TANK_SIZE > Constants.BOUND_HEIGHT)
			y = Constants.BOUND_HEIGHT - Constants.TANK_SIZE;
	}

	public void shut() {
		bullets.add(new Bullet(x + (int) (0.5 * Constants.TANK_SIZE) - (int) (0.5 * Constants.BULLET_SIZE),
				y + (int) (0.5 * Constants.TANK_SIZE) - (int) (0.5 * Constants.BULLET_SIZE), this));
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, Constants.TANK_SIZE, Constants.TANK_SIZE);
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		this.ptDirection = direction;
	}

	public Direction getPtDirection() {
		return ptDirection;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public Tank() {
		isAlive = true;
	}

}
