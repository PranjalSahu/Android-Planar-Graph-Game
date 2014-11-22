package com.example.arrowgameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameStartView extends View{

	private Handler h; 						// for animation sequence timing
	private Paint testPaint;
	
	private final int FRAME_RATE = 30;
	
	public GameStartView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		h = new Handler();
		testPaint = new Paint();
		testPaint.setColor(Color.BLACK);
		testPaint.setAntiAlias(true);
		testPaint.setStrokeWidth(10);		
		
		Graph.Makegraph();		// Generate the graph		
	}
	
	private Runnable r = new Runnable(){		
		public void run(){
			invalidate();
		}
	};
	
	protected void onDraw(Canvas c){		
		h.postDelayed(r, FRAME_RATE);
	}
}
