package cn.lumiaj.tankWar0_4.core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Utils;

public class Explode {
	private int x, y, step;
	private boolean isLive;
	
	public boolean paint(Graphics g) {
		if(!isLive) return isLive;
		Color c = g.getColor();
		g.setColor(Constants.EXPLODE_COLOR);
		g.drawImage(Utils.getImage("b/"+step+".png"), x, y, null);
		g.setColor(c);
		step++;
		if(step==10) {
			isLive = false;
			return isLive;
		}
		return isLive;
	}
	
	public Explode(int x, int y) {
		this.x = x-new Random().nextInt(30);
		this.y = y-new Random().nextInt(30);
		this.step = 1;
		this.isLive = true;
	}
}
