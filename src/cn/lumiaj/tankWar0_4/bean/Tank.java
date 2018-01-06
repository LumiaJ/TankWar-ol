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
	protected List<Bullet> bullets;
	protected Direction direction, ptDirection;
	protected Color bulletColor;
	protected Client client;
	protected boolean isAlive;

	public abstract void boom();

	public List<Bullet> getBullets() {
		return bullets;
	}

	public Direction getDirection() {
		return direction;
	}

	public Direction getPtDirection() {
		return ptDirection;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, Constants.TANK_SIZE, Constants.TANK_SIZE);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isAlive() {
		return isAlive;
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
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		if(direction!=Direction.S) {
			this.ptDirection = direction;
		}
	}

	public void shut() {
		bullets.add(new Bullet(x + (int) (0.5 * Constants.TANK_SIZE) - (int) (0.5 * Constants.BULLET_SIZE),
				y + (int) (0.5 * Constants.TANK_SIZE) - (int) (0.5 * Constants.BULLET_SIZE), this));
	}

	public Tank() {
		this.bullets = new ArrayList<Bullet>();
		isAlive = true;
	}

}
