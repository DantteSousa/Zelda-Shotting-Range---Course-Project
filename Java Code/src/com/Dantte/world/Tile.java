package com.Dantte.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.Dantte.main.Game;

public class Tile {
	//Getting sprites for floor  and wall from the spritesheet
	public static BufferedImage TILE_FLOOR = Game.Mapsheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.Mapsheet.getSprite(16, 0, 16, 16);
	private BufferedImage sprite;
	private int x,y;
	public Tile(int x,int y,BufferedImage sprite) {
		super();
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}
	//method to render tiles 	
	public void render(Graphics g) {
		g.drawImage(sprite, x-Camera.x, y -Camera.y, null);
	}
}
