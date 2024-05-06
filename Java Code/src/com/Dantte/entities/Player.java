package com.Dantte.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.Dantte.main.Game;
import com.Dantte.world.Camera;
import com.Dantte.world.World;

public class Player extends Entity{

	public boolean right,left,up,down;
	public double speed = 1.4 ;
	public int right_d=0,left_d=1,up_d=2,down_d=3;
	public int dir=down_d;
	private int frames=0,maxFrames=5,index=0,maxIndex=1;
	private boolean moved=false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	public double life = 100;
	public static double maxLife = 100;
	public static int ammo = 0, maxAmmo = 500;
	public static boolean haveBow = false;
	private BufferedImage[] playerDamage;
	public boolean isDamaged = false;
	private int damageFrame = 0;
	public boolean shooting = false;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		//creating arrays for player movement animations
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer  = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		//Creating sprites for damaging player feeedback
		playerDamage = new BufferedImage[4];
		playerDamage [0]= Game.Csheet.getSprite(0, 33, 16, 16);//UP
		playerDamage [1]= Game.Csheet.getSprite(16, 33, 16, 16);//DOWN
		playerDamage [2]= Game.Csheet.getSprite(0, 49, 16, 16);//LEFT		
		playerDamage [3]= Game.Csheet.getSprite(16, 49, 16, 16);//RIGHT
		//reading sprites and inputing in the movement arrays
		for(int i = 0;i<2;i++) {
			rightPlayer[i]=Game.Csheet.getSprite((32+(i*16)), 16, 16, 16);
		}
		for(int i = 0;i<2;i++) {
			leftPlayer[i]=Game.Csheet.getSprite((i*16), 16, 16, 16);
		}
		for(int i = 0;i<2;i++) {
			upPlayer[i]=Game.Csheet.getSprite((i*16), 0, 16, 16);
		}
		for(int i = 0;i<2;i++) {
			downPlayer[i]=Game.Csheet.getSprite((32+(i*16)), 0, 16, 16);
		}
	}
	// Method repeting every gameloop to check events with player
	public void tick() {
		moved=false;
		//Checking player collision with map and letting the player moving
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved=true;
			dir=right_d;
			x+=speed;
		
		}else if(left && World.isFree((int)(x-speed),this.getY())){
			moved=true;
			dir=left_d;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved=true;
			dir=up_d;
			y-=speed;
		}else if(down && World.isFree(this.getX(),(int)(y+speed))){
			moved=true;
			dir=down_d;
			y+=speed;
		}
		if(moved) {
			frames++;
			if (frames== maxFrames) {
				frames=0;
				index++;
				if(index>maxIndex) {
					index=0;
				}
			}
		}
		// Calling Methods for player collision
		this.PotionCollison();
		this.AmmoCollison();
		this.WeaponCollison();
		//Damaging flash animation
		if(isDamaged) {
			this.damageFrame++;
			if(this.damageFrame== 8) {
				this.damageFrame=0;
				isDamaged=false;
			}
		}
		//Game Over
		if(life<=0) {
			life=0;
			Game.gameState = "GAME_OVER";
			}
		//Shooting 
		 if(shooting) {
			 shooting =false;
			 if(haveBow && ammo!=0) {
				int spritenum = 0;
				int dx=0;
				int dy=0;
				if(dir == right_d) {
					dx=1;
					spritenum=2	;
				}else if(dir==left_d){
					dx =-1;
					spritenum=3	;
				}
				if(dir == up_d) {
					dy =-1;
					spritenum=0	;
				}else if(dir ==down_d) {
					dy=+1;
					spritenum=1	;
				}
				ammo--;
				Projectile projectiles = new Projectile(this.getX(),this.getY(),16,16,dx,dy,Projectile.projectileSprite[spritenum]);
				Game.projectiles.add(projectiles);
			 }					
		 }	
			
		
		//Camera Movement
		Camera.x = Camera.clamp(this.getX()-(Game.WIDTH/2),0,World.WIDTH*16-Game.WIDTH);
		Camera.y = Camera.clamp(this.getY()-(Game.HEIGHT/2),0,World.HEIGHT*16-Game.HEIGHT);
	}
	
	//Method for detect collision and collect ammo
	public void AmmoCollison() {
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Arrow)/*check if current entity is arrow entity*/ 
				{
				if(Entity.isColliding(this,current)) {
					ammo+=100;
					if(ammo>maxAmmo)
						ammo=maxAmmo;
					Game.entities.remove(current);
				}
				
			}
		}
	}
	//Method for potion coliision to Regain life
	public void PotionCollison() {
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Potion)/* check if current entity is potion entity*/{
				if(Entity.isColliding(this,current)) {
					life+=25;//restoring life to player
					if(life>maxLife)
						life=maxLife;//maintain full life not to pass the max life
					Game.entities.remove(current);
				}
				
			}
		}
	}
	//weapon collision and upgrade
	public void WeaponCollison() {
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Weapon)/* check if current entity is Weapon entity*/{
				if(Entity.isColliding(this,current)) {
					if(haveBow) {
						
					}else
					{
						haveBow = true;
						ammo+=25;
					}
					Game.entities.remove(current);
				}
				
			}
		}
	}
//----------Player rendering Sprites on movement
	public void render(Graphics g) {
	if(!isDamaged) {
		if(dir==right_d) { 
			g.drawImage(rightPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y,null);
			}else if(dir==left_d) {
			g.drawImage(leftPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y,null);
			}else if(dir==up_d) {
			g.drawImage(upPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y,null);
			}else if(dir==down_d) {
			g.drawImage(downPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y,null);
			}
		}else {
			if(dir==right_d) { 
				g.drawImage(playerDamage [3],this.getX()-Camera.x,this.getY()-Camera.y,null);
				}else if(dir==left_d) {
				g.drawImage(playerDamage [2],this.getX()-Camera.x,this.getY()-Camera.y,null);
				}else if(dir==up_d) {
				g.drawImage(playerDamage [0],this.getX()-Camera.x,this.getY()-Camera.y,null);
				}else if(dir==down_d) {
				g.drawImage(playerDamage [1],this.getX()-Camera.x,this.getY()-Camera.y,null);
				}
		}
	}

}
