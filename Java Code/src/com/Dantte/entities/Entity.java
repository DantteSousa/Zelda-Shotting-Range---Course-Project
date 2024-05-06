package com.Dantte.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.Dantte.main.Game;
import com.Dantte.world.Camera;

public class Entity {
	public static BufferedImage ENEMY = Game.EntitySheet.getSprite(32, 16, 16, 16);
	public static BufferedImage POTION = Game.EntitySheet.getSprite(16,0,16,16);
	public static BufferedImage BOW = Game.EntitySheet.getSprite(0,0,16,16);
	public static BufferedImage ARROW = Game.EntitySheet.getSprite(32,0,16,16);
	public static BufferedImage projectileSprite [];
	public static BufferedImage ENEMY_FEEDBACK = Game.EntitySheet.getSprite(0, 32, 16, 16);
	
	protected double x,y ;
	protected int width, height;
	private BufferedImage sprite;
	private int maskx,masky, mwidth, mheight;

	public Entity(int x, int y, int width, int height,BufferedImage sprite) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	
	public void setMask(int maskx, int masky, int mwidth, int mheight){
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
		}

	public int getX() { 
		return (int)x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y , null);
		//Code for testing HIT BoX
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y,mwidth,mheight);
		
	}
	public void tick() {
	}
	// method for collision with entities
	public static boolean isColliding(Entity e1,Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() +e1.maskx,e1.getY()+e1.masky,e1.mwidth,e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.mwidth,e2.mheight);
		return e1Mask.intersects(e2Mask);
	}	
}
