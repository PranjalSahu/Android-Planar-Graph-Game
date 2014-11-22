package com.example.arrowgameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ArrowGameView extends View{

	private Handler h; 						// for animation sequence timing
	public Actor mynodes[];					// Array of nodes in the gameplay
	public static Vertex myvertex[];		// Set of vertex only x and y coordinates
	private Paint testPaint;
	private Paint textPaint;				// for painting the text	
	
	static public int GameOver = 0;			// 1 means game is over i.e player has succeeded 
	private final int FRAME_RATE = 30;
	
	public static int twidth  = -1;
	public static int theight = -1;
	
	private int starttimeinsec = 0;
	
	public ArrowGameView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		h = new Handler();
		
		starttimeinsec = (int)System.currentTimeMillis()/1000;
		
		testPaint = new Paint();
		testPaint.setColor(Color.BLACK);
		testPaint.setAntiAlias(true);
		testPaint.setStrokeWidth(10);		
		
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK); 
		textPaint.setTextSize(64); 
		textPaint.setAntiAlias(true);
		testPaint.setStrokeWidth(10);
		
		Graph.Makegraph();		// Generate the graph
		
		mynodes   = new Actor[Graph.numnodes];
		myvertex  = new Vertex[Graph.numnodes];
		
		for(int i=0;i<Graph.numnodes;++i){
			int tx = Graph.randInt(0, 1000);
			int ty = Graph.randInt(0, 1000);
			mynodes[i]  = new Actor(context, tx, ty, "Bob", R.drawable.node2);
			myvertex[i] = new Vertex(tx, ty);
		}
	}
	
	private Runnable r = new Runnable(){
		
		public void run(){
			invalidate();
		}
	};
	
	public static int CheckGraphIntersection(){
		for(int i=0;i<Graph.totaledge.size()-1;++i){
			for(int j=i+1;j<Graph.totaledge.size();++j){
				Edge e1 = Graph.totaledge.get(i);
				Edge e2 = Graph.totaledge.get(j);				
				//System.out.println("e1.v1 = "+e1.v1+" e1.v2 = "+e1.v2+" e2.v1 = "+e2.v1+" e2.v2 = "+e2.v2);
				if(e1.v1 == e2.v1 || e1.v1 == e2.v2 || e1.v2 == e2.v1 || e1.v2 == e2.v2){
					//System.out.println("continue");
					continue;				
				}				
				
				if(Graph.doIntersect(myvertex[e1.v1], myvertex[e1.v2], myvertex[e2.v1], myvertex[e2.v2])){
					//System.out.println("INTERSECTION BETWEEN  EDGES "+i+" AND "+j);
					return 1;
				}
			}
		}
		return 0;
	}
	
	public boolean onTouchEvent(MotionEvent event){	
		int action     = event.getAction();
		int index      = event.getActionIndex();
		int pointerId  = event.getPointerId(index);
		
		System.out.println("ONTOUCH EVENT FROM ARROWGAMEVIEW  <<<<<<<<<<<");
		
		return false;
	}
	
	protected void onDraw(Canvas c){ 
	
		int ctime = (int)System.currentTimeMillis()/1000;
		int diff = ctime-starttimeinsec;
		
		int twidth  = mynodes[0].getBitmap().getWidth()/2; 
		int theight = mynodes[0].getBitmap().getHeight()/2;
		
		for(int i=0;i<Graph.numedges;++i){
			int v1 = Graph.totaledge.get(i).v1;
			int v2 = Graph.totaledge.get(i).v2;			 
			c.drawLine(mynodes[v1].getX()+twidth, mynodes[v1].getY()+theight, mynodes[v2].getX()+twidth, mynodes[v2].getY()+theight, testPaint);			
		}
		
		for(int i=0;i<Graph.numnodes;++i){
			c.drawBitmap(mynodes[i].getBitmap(), mynodes[i].getX(), mynodes[i].getY(), null);
		}
		
		c.drawText(Integer.toString(diff), 900, 100, textPaint); 
		
		h.postDelayed(r, FRAME_RATE);
	}
}
