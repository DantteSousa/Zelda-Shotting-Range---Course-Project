package com.Dantte.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.Dantte.Graphics.Spritesheet;
import com.Dantte.Graphics.UI;
import com.Dantte.entities.Enemy;
import com.Dantte.entities.Entity;
import com.Dantte.entities.Player;
import com.Dantte.entities.Projectile;
import com.Dantte.world.World;

public class Game extends Canvas implements Runnable,KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public Thread thread;
	private boolean isRunning =true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private int CUR_LEVEL = 1,MAX_LEVEL=5;
	
	private BufferedImage image;
	
	public static World world;
	
	public static List<Entity> entities; 
	public static List<Enemy> enemies; 
	public static List<Projectile> projectiles;
	public static Spritesheet Mapsheet;
	public static Spritesheet Csheet;
	public static Spritesheet EntitySheet;
	public static Spritesheet MenuBack;
	public static Player player;
	public static Random rand;
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	public Menu menu;
	public static int elapsedTime,score;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		Mapsheet = new Spritesheet("/res/Mapsheet.png");
		Csheet = new Spritesheet("/res/CharacterSprite.png");
		EntitySheet = new Spritesheet("/res/EntitiesSpriteSheet.png");
		MenuBack = new Spritesheet("/res/MenuBackground1.jpg");
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		//objects
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities =  new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectile>();
		player = new Player (0,0,16,16,Csheet.getSprite(48,0, 16, 16));
		entities.add(player);
		
		//world
		world = new World("/res/level1.png");
		
		//Game Menu
		menu = new Menu();
		
		
	}

	public void initFrame() {		
		frame = new JFrame("Dantte - 300330399 - ARP - 4495");
		frame.add(this);
		frame.setAutoRequestFocus(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() {
		isRunning=false;
		try { 
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game game = new Game();
		game.start();
	}
	
	// GAME MAIN Tick, method for gameloop to make the game run
	public void tick() {
		if(gameState == "NORMAL"){
			this.restartGame = false;
			for(int i =0;i<entities.size();i++){
				Entity e = entities.get(i);
				e.tick();
				}
			for(int i =0;i<projectiles.size();i++){
				projectiles.get(i).tick();
				}
			if(enemies.size()==0) {
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "res/level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
				System.out.println(elapsedTime);;
			}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) 
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			if(restartGame) {
				this.restartGame = false;
				Game.gameState = "MENU";
				CUR_LEVEL=1;
				String newWorld = "/res/level"+ CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			menu.tick();
			
		}
	}
	
	// GAME MAIN RENDER
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
 
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,WIDTH,HEIGHT);

		/*Render game*/
		//Map Render
		world.render(g);
		
		//Entities render
		for(int i =0;i<entities.size();i++){
			Entity e = entities.get(i);
			e.render(g);
			}
		for(int i =0;i<projectiles.size();i++){
			projectiles.get(i).render(g);
			}
		
		//UI rendering
		ui.renderLifeBar(g);
		ui.renderAmmo(g);
		ui.renderScore(g);
		ui.renderTime(g);
		
		g.dispose();//method to clean
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		//Game Over Screen
		if(gameState == "GAME_OVER") {
			Graphics g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial",Font.BOLD,36));
			g.setColor(Color.white);
			g.drawString("GAME OVER",((WIDTH*SCALE/2) -90),(HEIGHT*SCALE/2) +10);
			g.setFont(new Font("arial",Font.BOLD,28));
			if(showMessageGameOver) {
			g.drawString("Press ENTER to restart",((WIDTH*SCALE/2) -150),(HEIGHT*SCALE/2) +40);
			}
		}else if(gameState == "MENU"){// GAME MENU
			menu.render(g);
		}
		//render all game
		bs.show();
	}
	@Override
	//RUN gameLoop------------------------------
	public void run() {
		// TODO Auto-generated method stub
		//Game Loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) /ns;
			lastTime = now ;
			if (delta >=1 ){
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() -  timer >=1000) {
				System.out.println("FPS: "+frames);
				frames=0;
				timer+=1000;
				if(gameState!="GAME_OVER") {
						elapsedTime++;
				}
			}
		}
		stop();
	}
// player keybindings --------------------------------------------------------------------------------------
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
		}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//Player Movement Right or Left
		if(e.getKeyCode()== KeyEvent.VK_RIGHT||
				e.getKeyCode()== KeyEvent.VK_D) {
			//RIGHT-----------------------------------------
			player.right = true;
			
		}else if(e.getKeyCode()== KeyEvent.VK_LEFT||
				e.getKeyCode()== KeyEvent.VK_A) {
				//LEFT-----------------------------------------
			player.left = true;
			
	       }
	
		//UP Arrow PRESSED-----------------------------------------
		if(e.getKeyCode()== KeyEvent.VK_UP||
				e.getKeyCode()== KeyEvent.VK_W) {
					player.up = true;
					if(gameState == "MENU") {
						menu.up = true;
					}
			}//DOWN ARROW PRESSED-----------------------------------------	
		else if(e.getKeyCode()== KeyEvent.VK_DOWN||
			e.getKeyCode()== KeyEvent.VK_S) {	
				player.down = true;
				if(gameState == "MENU") {
					menu.down = true;
				}
			}
		
		//SPACE PRESSED
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shooting=true;
		}
		//ENTER PRESSED
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if( gameState == "MENU") {
				menu.enter = true;
			}
			
		}
		//game exit esc
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			gameState= "MENU";
			menu.pause = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//Player Movement Right or Left
		if(e.getKeyCode()== KeyEvent.VK_RIGHT||
				e.getKeyCode()== KeyEvent.VK_D) {
			//RIGHT-----------------------------------------
			player.right = false;
			
		}else if(e.getKeyCode()== KeyEvent.VK_LEFT||
				e.getKeyCode()== KeyEvent.VK_A) {
				//LEFT-----------------------------------------
			player.left = false;
			
	       }
		//player movement Up and Down-----------------------------------------
		if(e.getKeyCode()== KeyEvent.VK_UP||
				e.getKeyCode()== KeyEvent.VK_W) {
		//UP-----------------------------------------
			player.up = false;
			
		}else if(e.getKeyCode()== KeyEvent.VK_DOWN||
				e.getKeyCode()== KeyEvent.VK_S) {	
		//DOWN-----------------------------------------	
			player.down = false;
	       }
	}

}
;