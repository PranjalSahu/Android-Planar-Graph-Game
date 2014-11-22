package com.example.arrowgameengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable; // read about it

public class Actor {
	private Context mContext;
	
	private int x;			// Location of actor
	private int y;
	
	private String name;	//name
	
	private int costume;	// refers to drawable resources
	private int currentCostume;
	
	private BitmapDrawable[] graphic = new BitmapDrawable[10];
	
	public Actor(Context context, int xSet, int ySet, String n, int outfit){
		x = xSet;
		y = ySet;
		name = n;
		costume = outfit;
		currentCostume = 0;
		mContext  = context;
		graphic[0] = (BitmapDrawable)mContext.getResources().getDrawable(costume);		
	}
	
	public void goTo(int xPos, int yPos){	// set functions
		x = xPos;
		y = yPos;		
	}
	
	public void setCostume(int c, int i){
		graphic[i] = (BitmapDrawable)mContext.getResources().getDrawable(c);
	}

	public void setCurrentCostume(int i){
		currentCostume = i;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCostume(){
		return costume;
	}
	
	public Bitmap getBitmap(){
		return graphic[currentCostume].getBitmap();
	}
	
	public Bitmap getBitMapAtIndex(int i){
		return  graphic[i].getBitmap();
	}
}
