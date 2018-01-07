package cn.lumiaj.tankWar0_4.bean;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.tankWar0_4.udpPackage.BulletDieMsg;
import cn.lumiaj.tankWar0_4.udpPackage.TankHPChangeMsg;
import cn.lumiaj.tankWar0_4.udpPackage.TankMoveMsg;
import cn.lumiaj.tankWar0_4.udpPackage.TankSpeedChangeMsg;
import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Direction;
import cn.lumiaj.utils.Utils;

public class Player extends Tank {
	private boolean leftMove, rightMove, upMove, downMove, primary;
	private int HP, bigbangCount;
	
	private void isHitted() {
		if(client.isOnline() && primary) {
			for(int i=0;i<client.getOther().size();i++) {
				List<Bullet> enemyBullets = client.getOther().get(i).getBullets();
				for(int j=0;j<enemyBullets.size();j++) {
					if(this.getRect().intersects(enemyBullets.get(j).getRect())) {
						this.boom();
						this.client.getNc().send(new BulletDieMsg(client.getOther().get(i).getId(), j));
					}
				}
			}
		}
	}

	public void bigbang() {
		Direction mark = ptDirection;
		for (Direction d : Direction.values()) {
			if (d != Direction.S) {
				ptDirection = d;
				shut();
			}
		}
		ptDirection = mark;
		HP -= 5;
		if(client.isOnline())
			client.getNc().send(new TankHPChangeMsg(HP, id));
		// bigbangCount--;
	}

	@Override
	public void boom() {
		HP -= 10;
		if(client.isOnline())
			client.getNc().send(new TankHPChangeMsg(HP, id));
		if (HP <= 0) {
			client.gameOver();
		}
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		// move
		switch (code) {
		case KeyEvent.VK_LEFT:
			leftMove = true;
			redirection();
			break;
		case KeyEvent.VK_RIGHT:
			rightMove = true;
			redirection();
			break;
		case KeyEvent.VK_UP:
			upMove = true;
			redirection();
			break;
		case KeyEvent.VK_DOWN:
			downMove = true;
			redirection();
			break;
		case KeyEvent.VK_S:
			speed *= 2;
			if (client.isOnline())
				client.getNc().send(new TankSpeedChangeMsg(speed, client));
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		// stop
		switch (code) {
		case KeyEvent.VK_LEFT:
			leftMove = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightMove = false;
			break;
		case KeyEvent.VK_UP:
			upMove = false;
			break;
		case KeyEvent.VK_DOWN:
			downMove = false;
			break;
		case KeyEvent.VK_A:
			if (bullets.size() <= 4)
				shut();
			break;
		case KeyEvent.VK_S:
			speed = Constants.PLAYER_SPEED;
			if (client.isOnline())
				client.getNc().send(new TankSpeedChangeMsg(speed, client));
			break;
		case KeyEvent.VK_Q:
			if (bigbangCount > 0) {
				bigbang();
			}
			break;
		}
		redirection();
	}

	public void paint(Graphics g) {
		if (HP <= 30) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawString("HP:" + HP, x, y - 18);
			g.setColor(c);
		} else {
			g.drawString("HP:" + HP, x, y - 18);
		}
		isHitted();
		g.drawString("ID:" + id, x, y - 3);
		Color c = g.getColor();
		g.setColor(Constants.PLAYER_COLOR);
		// g.drawImage(Utils.getImage("p/1.png"),x,y,null);
		move();
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint(g);
		}
		paintPT(g, ptDirection);
		g.setColor(c);
	}

	@Override
	public void paintPT(Graphics g, Direction dir) {
		switch (dir) {
		case U:
			g.drawImage(Utils.getImage("p/1.png"), x, y, null);
			break;
		case D:
			g.drawImage(Utils.getImage("p/5.png"), x, y, null);
			break;
		case L:
			g.drawImage(Utils.getImage("p/7.png"), x, y, null);
			break;
		case R:
			g.drawImage(Utils.getImage("p/3.png"), x, y, null);
			break;
		case UR:
			g.drawImage(Utils.getImage("p/2.png"), x, y, null);
			break;
		case UL:
			g.drawImage(Utils.getImage("p/8.png"), x, y, null);
			break;
		case DR:
			g.drawImage(Utils.getImage("p/4.png"), x, y, null);
			break;
		case DL:
			g.drawImage(Utils.getImage("p/6.png"), x, y, null);
			break;
		case S:
			break;
		}
	}

	public void redirection() {
		Direction oldDir = direction;

		if (leftMove && !rightMove && !upMove && !downMove)
			direction = Direction.L;
		else if (!leftMove && rightMove && !upMove && !downMove)
			direction = Direction.R;
		else if (!leftMove && !rightMove && upMove && !downMove)
			direction = Direction.U;
		else if (!leftMove && !rightMove && !upMove && downMove)
			direction = Direction.D;
		else if (leftMove && !rightMove && upMove && !downMove)
			direction = Direction.UL;
		else if (leftMove && !rightMove && !upMove && downMove)
			direction = Direction.DL;
		else if (!leftMove && rightMove && upMove && !downMove)
			direction = Direction.UR;
		else if (!leftMove && rightMove && !upMove && downMove)
			direction = Direction.DR;
		else if (!leftMove && !rightMove && !upMove && !downMove)
			direction = Direction.S;
		if (direction != Direction.S) {
			ptDirection = direction;
		}

		if (oldDir != direction && client.isOnline()) {
			client.getNc().send(new TankMoveMsg(this.id, x, y, direction));
		}

	}
	
	/**
	 * 通过接受服务器指令调用Others中坦克的血量设置，Player不调用此方法
	 * @param hP
	 */
	public void setHP(int hP) {
		HP = hP;
	}
	
	/**
	 * 用于和Other区分开，在检测是否被击中时
	 * @param primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public Player(int x, int y, Client client) {
		this(666, x, y, Direction.S, client);
	}

	public Player(int id, int x, int y, Direction direction, Client client) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.client = client;
		this.speed = Constants.PLAYER_SPEED;
		this.bulletColor = Constants.PLAYER_BULLET_COLOR;
		this.ptDirection = Direction.U;
		this.HP = 100;
		this.bigbangCount = 3;
		this.primary = false;
	}

}
