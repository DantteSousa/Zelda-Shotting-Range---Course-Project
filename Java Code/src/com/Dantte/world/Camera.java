package com.Dantte.world;

public class Camera {
	
	public static int x;
	public static int y;
	public static int clamp(int xCurrent,int xMin,int xMax) {
		if(xCurrent < xMin ) {
			xCurrent = xMin;
			
		}
		if(xCurrent >xMax) {
			xCurrent = xMax;
		}
			return xCurrent;
	}
}
