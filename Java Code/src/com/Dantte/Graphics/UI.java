package com.Dantte.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.Dantte.entities.Player;
import com.Dantte.main.Game;

public class UI {
	public void renderLifeBar(Graphics g){
	//Player Life Bar
		//Unfilled life bar
		g.setColor(Color.black);
		g.fillRect(8, 4, 70, 8);
		//Life Bar
		g.setColor(Color.RED);
		g.fillRect(8, 4, (int)((Game.player.life/Player.maxLife)*70), 8);
		//Life bar Number UI
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,7));
		g.drawString((int)Game.player.life+"/"+(int)Player.maxLife,9,11);
	}
	
	public void renderAmmo(Graphics g) {
	//Player Ammo
	g.setColor(Color.yellow);
	g.setFont(new Font("arial",Font.BOLD,9));
	if(Player.haveBow) 
	{g.drawImage(Game.EntitySheet.getSprite(0,0,16,16),195,14,null);}
	g.drawString("Arrows:"+Player.ammo,190,13);
	}
	
	public void renderScore(Graphics g) {
		//game Score
		g.setColor(Color.RED);
		g.setFont(new Font("arial",Font.BOLD,9));
		g.drawString("SCORE: "+Game.score,80,8);
	}
	
	public void renderTime(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("arial",Font.BOLD,9));
		g.drawString("TIME: "+Game.elapsedTime,130,8);
	}
	
}
