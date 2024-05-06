package com.Dantte.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.Dantte.Graphics.Spritesheet;
import com.Dantte.entities.*;
import com.Dantte.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	//Method for Map Structure
	public World(String path) {
		try {
			//reading file in the path string 
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			//creating array to inserts file information
			int[] pixelMap = new int[WIDTH*HEIGHT];
			//creating array to inserts tile positions;
			tiles = new Tile[WIDTH*HEIGHT];
			
			//inputing information from file into the array;
			map.getRGB(0, 0, WIDTH, HEIGHT, pixelMap, 0, WIDTH);
			//reading array to form map
			for(int xx = 0;xx<WIDTH;xx++) {//For loop for X axis on map file
				for(int yy = 0;yy<HEIGHT;yy++) {//For loop for Y axis on map file
					int curentPixel = pixelMap[xx+(yy*WIDTH)];//Current pixel array for easy use
					//int previousXPixel = pixelMap[(xx-1)+(yy*WIDTH)];
					//int previousYPixel = ;
					//Pixel check for map formation-----------------------------------------------------------------
					if(curentPixel == 0xFF000000) {//checking if pixel on file is black
						//Floor
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(curentPixel== 0xFFFFFFFF) {//checking if pixel on file is white
						//Wall
						tiles[xx+(yy*WIDTH)]= new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if(curentPixel== 0xFF00137F){
						//Player
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						
					}else if(curentPixel== 0xFFFF0000){
						//enemy
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY);
						Game.entities.add(en);
						Game.enemies.add(en);
						
						
					}else if(curentPixel== 0xFFFF6A00){
						//BOW
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Weapon bow = new Weapon(xx*16,yy*16,16,16,Entity.BOW);
						bow.setMask(1, 1, 14, 14);
						Game.entities.add(bow);
						
					}else if(curentPixel== 0xFFFFD800){
						//ARROW
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Arrow arrow = new Arrow(xx*16,yy*16,16,16,Entity.ARROW);
						arrow.setMask(6,1,6,14);
						Game.entities.add(arrow);
						
						
					}else if(curentPixel== 0xFF00FF21){
						//POTION
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Potion potion = new Potion(xx*16,yy*16,16,16,Entity.POTION);
						potion.setMask(6,2,6,13);
						Game.entities.add(potion);
						
					}else{
						tiles[xx+(yy*WIDTH)]= new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}
						
				}//endfory
			}//endforx
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end world method
	public static boolean isFree(int xnext,int ynext ) {
	int x1 = xnext/TILE_SIZE;
	int y1 = ynext/TILE_SIZE;
	
	int x2 = (xnext+TILE_SIZE-1)/TILE_SIZE;
	int y2 = ynext/TILE_SIZE;
	
	int x3 = xnext/TILE_SIZE;
	int y3 = (ynext+TILE_SIZE-1)/TILE_SIZE;
	
	int x4 = (xnext+TILE_SIZE-1)/TILE_SIZE;
	int y4 = (ynext+TILE_SIZE-1)/TILE_SIZE;
	
	return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
			(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
			(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
			(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
			
	
	} 
	public static void restartGame(String level) {
		Game.Mapsheet = new Spritesheet("/res/Mapsheet.png");
		Game.Csheet = new Spritesheet("/res/CharacterSprite.png");
		Game.EntitySheet = new Spritesheet("/res/EntitiesSpriteSheet.png");
		Game.entities =  new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.player = new Player (0,0,16,16,Game.Csheet.getSprite(48,0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
//Method for Map render
	public void render(Graphics g) {
		
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH/16);
		int yfinal = ystart + (Game.HEIGHT/16);
		
		for(int xx = xstart;xx<=xfinal;xx++) {//For loop for X axis on map file
			for(int yy = ystart;yy<=yfinal;yy++){
				if(xx<0 || yy<0 || xx>=WIDTH || yy>=HEIGHT)
					continue;
				
				
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	
}// end class
