package com.example.arrowgameengine;

public class Vertex {
	public int x;
	public int y;
	public int line1;
	public int line2;
	public int id;
	
	public Vertex(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vertex(int x, int y, int line1, int line2, int id){
		this.x     = x;
		this.y     = y;
		this.line1 = line1;
		this.line2 = line2;
		this.id    = id;
	}	
	
}
