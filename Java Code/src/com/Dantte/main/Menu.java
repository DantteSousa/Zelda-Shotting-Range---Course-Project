package com.Dantte.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	public String[] options = {"New Game","Exit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up,down,enter,pause;
	
	public void tick(){
		if(up){
			up =false;
			currentOption --;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down=false;
			currentOption ++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(pause) {
			options[0]="Continue";
		}
		if(enter) {
			enter = false;
			if(options[currentOption]==options[0]) {
				Game.gameState = "NORMAL";
			}
			if(options[currentOption]==options[1]) {
				System.exit(1);
			}

		}
	}
	
	public void render(Graphics g)  {
		//g.setColor(Color.black);
		//g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		//Image resultingImage = Game.MenuBack.getSprite(0, 0, 720,540).getScaledInstance((Game.WIDTH*Game.SCALE),(Game.HEIGHT*Game.SCALE), Image.SCALE_SMOOTH);
		//g.drawImage(resultingImage,0,0,null);
		g.drawImage(Game.MenuBack.getSprite(0, 0, 720,540),0,0,null);
		
		g.setColor(Color.red);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString("Zelda Shooting Ground",((Game.WIDTH*Game.SCALE/2)-200),(Game.HEIGHT*Game.SCALE/2)-120);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,15));
		g.drawString("4495 - Applied Researd Project",((Game.WIDTH*Game.SCALE/2)-10),(Game.HEIGHT*Game.SCALE/2)-100);
		g.drawString("Dantte Ferreira S. de Sousa - 300330399",((Game.WIDTH*Game.SCALE/2)-10),(Game.HEIGHT*Game.SCALE/2)-80);
		
		//Game MENU
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString(options[0],((Game.WIDTH*Game.SCALE/2)),(Game.HEIGHT*Game.SCALE/2));
		g.drawString(options[1],((Game.WIDTH*Game.SCALE/2)),(Game.HEIGHT*Game.SCALE/2)+30);

	
		if(options[currentOption]==options[0]){
			g.drawString(">>",((Game.WIDTH*Game.SCALE/2)-30),(Game.HEIGHT*Game.SCALE/2));
		}else if(options[currentOption]==options[1]){
			g.drawString(">>",((Game.WIDTH*Game.SCALE/2)-30),(Game.HEIGHT*Game.SCALE/2)+30);
		}
	}
}
