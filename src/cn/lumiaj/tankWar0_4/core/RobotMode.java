package cn.lumiaj.tankWar0_4.core;

import cn.lumiaj.tankWar0_4.bean.Robot;

public class RobotMode implements Runnable{
	private Robot r;
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(3000);
				if(!r.isAlive()) {
					r.boom();
				}else {
					autoBehavior();
					Thread.sleep(1000);
				}
			} catch (InterruptedException e1) {
			}
		}
	}
	
	public void autoBehavior() {
		try {
			r.redirection();
			r.shut();
			Thread.sleep(1000);
			r.redirection();
			r.move();
			Thread.sleep(1000);
			r.redirection();
			r.move();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public RobotMode(Robot r) {
		this.r = r;
	}

}
