package com.example.arrowgameengine;

/*
 * view uses gesture detector to detect gestures like touch events
 * 
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;		// for calculating the velocity of the touch
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * Generate n lines
 * Calculate intersection of all pairs
 * Label each point of intersection...these are the point of intersection
 * 	Also note which two lines intersection resulted in that vertex
 * 	Make set of vertex for each line
 * 
 * Consider each line one by one and sort the vertexes either by x or by y coordinate
 * 	 join the adjacent pairs and insert the edges in the hash set indexed by the two lines which resulted in that vertes
 * 
 * Arrange all the vertex around a circle randomly and make those edges
 * 
 * YO That's all !!!!
 * 
 */

public class ArrowGame extends Activity {

	private ArrowGameView arrowGameView;
	GestureDetector       gestureDetector;
	int selectednode;
	private VelocityTracker mVelocityTracker = null;		
	ImageView image;
	
	int currentmenu = 0;	// 0 means that the current view is start menu therefore if back is pressed then close
							// 1 means that the current view is of gameplay then if back is pressed then go to start menu
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		image = (ImageView) findViewById(R.id.test_image);
		setContentView(R.layout.start_menu);
		currentmenu = 0;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
        	if(currentmenu == 0){
        		System.out.println();
        		return super.onKeyDown(keyCode, event); 
        	}
        	else if(currentmenu == 1){
        		currentmenu = 0;
        		setContentView(R.layout.start_menu);
        		return false;
        	}
        }
        return super.onKeyDown(keyCode, event);
   }
	
	public void sendMessage(View view) {
	    //Intent intent = new Intent(this, DisplayMessageActivity.class);
		//image.setImageResource(R.drawable.startmenu);	
		currentmenu = 1;		
		setContentView(R.layout.activity_arrow_game);
		arrowGameView   = (ArrowGameView) findViewById(R.id.arrowGameView);
		gestureDetector = new GestureDetector(this, gestureListener);		
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arrow_game, menu);
		return true;
	}

	public boolean onTouchEvent(MotionEvent event){	
		int action     = event.getAction();
		int index      = event.getActionIndex();
		int pointerId  = event.getPointerId(index);
		
		int nodewidth  = arrowGameView.mynodes[0].getBitmap().getWidth();
		int nodeheight = arrowGameView.mynodes[0].getBitmap().getHeight();		
		
		if(action == MotionEvent.ACTION_DOWN){
			if(mVelocityTracker == null) {                
                mVelocityTracker = VelocityTracker.obtain();
            }
            else {                
                mVelocityTracker.clear();
            }            
            mVelocityTracker.addMovement(event);            
            
			int y = (int)event.getRawY();
			int x = (int)event.getRawX();			
			
			int min  = 10000;
			int mini = -1;
			
			for(int i=0;i<Graph.numnodes;++i){		
				int t1 = y-arrowGameView.mynodes[i].getY()-nodeheight/2;
				int t2 = x-arrowGameView.mynodes[i].getX()-nodewidth/2;
				int temp = (int) Math.sqrt(Math.abs(t1*t1+t2*t2));				
				if( temp < min){
					min = temp;
					mini = i;
				}
			}
			
			if(min < 5000){				
				arrowGameView.mynodes[mini].goTo(x-nodewidth, y-nodeheight);
				ArrowGameView.myvertex[mini].x = x-nodewidth;
				ArrowGameView.myvertex[mini].y = y-nodeheight;
				selectednode = mini;
			}
			else{
				selectednode = -1;				
			}
		}
		else if(action == MotionEvent.ACTION_MOVE){
			mVelocityTracker.addMovement(event);
			mVelocityTracker.computeCurrentVelocity(1000);
			int xvel = (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
			int yvel = (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
			
			int vel = (int) Math.sqrt(xvel*xvel+yvel*yvel);
			
			int y = (int)event.getRawY();
			int x = (int)event.getRawX();
			if(selectednode!=-1 && vel < 5000){
				arrowGameView.mynodes[selectednode].goTo(x-nodewidth, y-nodeheight);
				ArrowGameView.myvertex[selectednode].x = x-nodewidth;
				ArrowGameView.myvertex[selectednode].y = y-nodeheight;
			}
			else{
				selectednode = -1;
			}
		}
		else if(action == MotionEvent.ACTION_UP){
			int flag = ArrowGameView.CheckGraphIntersection();
			if(flag == 1){
				System.out.println("GRAPH HAS INTERSECTION");				
			}
			else{
				ArrowGameView.GameOver = 1;		// game done
				Toast.makeText(getApplicationContext(),"CONGRATULATIONS", 
		                Toast.LENGTH_LONG).show();
				setContentView(R.layout.start_menu);
				System.out.println("GRAPH HAS NO INTERSECTION");
				return false;	
			}
		}
		
		return false;
	}	
		
	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener(){		
		public boolean onDoubleTap(MotionEvent e){
			//arrowGameView.arrow.goTo(arrowGameView.archer.getX(), arrowGameView.archer.getY()+40);
			return true;
		}
	};
	
}
