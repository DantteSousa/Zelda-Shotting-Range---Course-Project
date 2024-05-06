package com.Dantte.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.Dantte.main.Game;
import com.Dantte.world.Camera;
import com.Dantte.world.World;

public class Enemy extends Entity{
	private double speed  = 1;
	private int maskx = 8;
	private int masky = 8;
	private int maskh = 10;
	private int maskw = 10;
	private int life = 5;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	
	private int frames=0,maxFrames=20,index=0,maxIndex=3;
	private BufferedImage[] enemyAnimations;	
	
	public Enemy(int x, int y, int width, int height, BufferedImage enemyAnimation) {
		super(x, y, width, height, null);
		enemyAnimations = new BufferedImage[4];
		enemyAnimations [0] = Game.EntitySheet.getSprite(32, 16, 16, 16);
		enemyAnimations [1] = Game.EntitySheet.getSprite(16, 16, 16, 16);
		enemyAnimations [2] = Game.EntitySheet.getSprite(0, 16, 16, 16);
		enemyAnimations [3] = Game.EntitySheet.getSprite(16, 16, 16, 16);
	}
	
	public void tick() {
	 // Enemy movement
	if(this.isCollidingWithPlayer()==false) {
			if ((int)x< Game.player.getX() && World.isFree((int)(x+speed),this.getY())
					&& !isColidding((int)(x+speed),this.getY())) {
			x+=(speed-0.4);
			}
			else if((int)x>Game.player.getX() && World.isFree((int)(x-speed),this.getY())
					&& !isColidding((int)(x-speed),this.getY())) {
			x-=(speed-0.4);
			}
			if ((int)y < Game.player.getY()&& World.isFree(this.getX(),(int)(y+ speed))
					&& !isColidding(this.getX(),(int)(y+speed))){
			y+=(speed-0.4);
			}
			else if((int)y>Game.player.getY()&& World.isFree(this.getX(),(int)(y-speed))
					&& !isColidding(this.getX(),(int)(y- speed))) {
			y-=(speed-0.4);
			}
		}else {
			//enemy colliding with player
			if (Game.rand.nextInt(100) < 10) {
				Game.player.life--;
				Game.player.isDamaged = true;
			}
		}
	//Animation Frames
			frames++;
			if (frames== maxFrames) {
				frames=0;
				index++;
				if(index>maxIndex) {
						index=0;
					}
				}
			CollidingArrow();
			
			if(life<=0) {
				destroySelf();
				Game.score+=10;
			}
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged=false;
				}
			}
			
	}
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void CollidingArrow() {
		for(int i = 0; i < Game.projectiles.size(); i++) {
			Entity e = Game.projectiles.get(i);
			if(e instanceof Projectile) {
				if(Entity.isColliding(this, e)) {
					isDamaged = true;
					life--;
					Game.projectiles.remove(i);
					return;
				}
			
			}
		}
	}
	//Method for Check collision with player 
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return enemyCurrent.intersects(player);
	}
	//Method for checking enemy Collision
	public boolean isColidding(int xnext,int ynext) {
		//Method for checking enemy Collision
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		for (int i = 0; i<Game.enemies.size();i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx,e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	//Method For redenring enenmy graphics
	public void render(Graphics g) {
		if(!isDamaged) {
		g.drawImage(enemyAnimations[index],this.getX() - Camera.x, this.getY() - Camera.y,null);
		}else {
		g.drawImage(Entity.ENEMY_FEEDBACK,this.getX() - Camera.x, this.getY() - Camera.y,null);
	
		}
	}

}
