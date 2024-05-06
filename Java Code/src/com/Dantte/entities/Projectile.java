package com.Dantte.entities;

import java.awt.image.BufferedImage;

import com.Dantte.main.Game;

public class Projectile extends Entity{
	//projectile direction
	private int dx,dy;
	//projectile speed
	private double spd = 4;
	//Projectile life
	private int lifep = 50,curLife = 0 ;
	//array of sprites of arrows in different directions
	public static BufferedImage[] projectileSprite = {
	 Game.EntitySheet.getSprite(48,0,16,16),// up = 0
	 Game.EntitySheet.getSprite(48,16,16,16),//down = 1
     Game.EntitySheet.getSprite(64,0,16,16), //right =2
	 Game.EntitySheet.getSprite(64,16,16,16)};//left =3}

	
	
	
	public Projectile(int x, int y, int width, int height,int dx,int dy, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.dx=dx;
		this.dy=dy;
	}
	
	public double getSpd() {
		return spd;
	}

	public void setSpd(double spd) {
		this.spd = spd;
	}

	public void tick(){
	x+=dx*spd;
	y+=dy*spd;
	curLife++;
	if(curLife==lifep) {
		Game.projectiles.remove(this);
		return;
	}
	}
	//public void render (Graphics g) {
		//g.setColor(Color.RED);
		//g.fillOval(this.getX()-Camera.x,this.getY()-Camera.y, width, height);
		
//	g.drawImage(sprite, this.getX()-Camera.x,this.getY()-Camera.y,null);	
	//Game.EntitySheet.getSprite(48,0,16,16); up
	//Game.EntitySheet.getSprite(64,0,16,16); right
	//Game.EntitySheet.getSprite(48,16,16,16);down
	//Game.EntitySheet.getSprite(64,16,16,16); left
	
}
