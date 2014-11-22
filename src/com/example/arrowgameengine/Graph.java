package com.example.arrowgameengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Graph {

	public static int intersectx;
	public static int intersecty;
	public static Comparator<Vertex> mycomp;
	
	public static ArrayList<Vertex>[] vertexlist;	// Array List of vertex for each line
	public static ArrayList<Vertex>   totalvertex;	// Array List of all the vertices present in the graph
	public static ArrayList<Edge>     totaledge;	// Array List of all the edges present in the graph	
	
	public static int numnodes;
	public static int numedges;
	public static int numlines = 5;
	public static int[][] lineset;
	
	public static boolean onSegment(Vertex p, Vertex q, Vertex r)
	{
	    if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
	        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
	       return true;
	 
	    return false;
	}
	
	public static int orientation(Vertex p, Vertex q, Vertex r)
	{
	    // See 10th slides from following link for derivation of the formula
	    // http://www.dcs.gla.ac.uk/~pat/52233/slides/Geometry1x1.pdf
	    int val = (q.y - p.y) * (r.x - q.x) -
	              (q.x - p.x) * (r.y - q.y);
	 
	    if (val == 0) return 0;  // colinear
	 
	    return (val > 0)? 1: 2; // clock or counterclock wise
	}
	
	// The main function that returns true if line segment 'p1q1'
	// and 'p2q2' intersect.
	public static boolean doIntersect(Vertex p1, Vertex q1, Vertex p2, Vertex q2)
	{
	    // Find the four orientations needed for general and
	    // special cases
	    int o1 = orientation(p1, q1, p2);
	    int o2 = orientation(p1, q1, q2);
	    int o3 = orientation(p2, q2, p1);
	    int o4 = orientation(p2, q2, q1);
	 
	    // General case
	    if (o1 != o2 && o3 != o4)
	        return true;
	 
	    // Special Cases
	    // p1, q1 and p2 are colinear and p2 lies on segment p1q1
	    //if (o1 == 0 && onSegment(p1, p2, q1)) return true;
	 
	    // p1, q1 and p2 are colinear and q2 lies on segment p1q1
	    //if (o2 == 0 && onSegment(p1, q2, q1)) return true;
	 
	    // p2, q2 and p1 are colinear and p1 lies on segment p2q2
	    //if (o3 == 0 && onSegment(p2, p1, q2)) return true;
	 
	     // p2, q2 and q1 are colinear and q1 lies on segment p2q2
	    //if (o4 == 0 && onSegment(p2, q1, q2)) return true;
	 
	    return false; // Doesn't fall in any of the above cases
	}
	 
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	//A = y2-y1
	//B = x1-x2
	//C = A*x1+B*y1
	//double det = A1*B2 - A2*B1
	
	public static void getintersection(int x11, int y11, int x12, int y12, int x21, int y21, int x22, int y22){
		int A1 = y12-y11;
		int B1 = x11-x12;
		int C1 = A1*x11+B1*y11;
		
		int A2 = y22-y21;
		int B2 = x21-x22;
		int C2 = A2*x21+B2*y21;
		
		float det = A1*B2-A2*B1;		
		if(det == 0){
			intersectx = -5000;	// lines are parallel
			intersecty = -5000;
		}else{
			  intersectx = (int) ((B2*C1 - B1*C2)/det);
			  intersecty = (int) ((A1*C2 - A2*C1)/det);
			  return;
		}
		return;
	}
	
	
	public static void deleteanode(){
		
	}
	
	public static void Makegraph(){
		vertexlist      = new ArrayList[numlines];
		totalvertex     = new ArrayList<Vertex>();
		totaledge       = new ArrayList<Edge>();
		
		//Generating the set of lines
		lineset = new int [Graph.numlines][4];
		for(int i=0;i<Graph.numlines;++i){
			int t1x = randInt(-3000, 3000);
			int t1y = randInt(-3000, 3000);
			int t2x = randInt(-3000, 3000);
			int t2y = randInt(-3000, 3000);
			lineset[i][0] = t1x;
			lineset[i][1] = t1y;
			lineset[i][2] = t2x;
			lineset[i][3] = t2y;
			vertexlist[i] = new ArrayList<Vertex>();
		}
		
		int vertexcount = 0;
		
		// Getting intersection
		for(int i=0;i<numlines;++i){
			for(int j=i+1;j<numlines;++j){
				getintersection(lineset[i][0], lineset[i][1], lineset[i][2], lineset[i][3], lineset[j][0], lineset[j][1], lineset[j][2], lineset[j][3]);
				int tempx = intersectx;
				int tempy = intersecty;
				Vertex t1 = new Vertex(tempx, tempy, i, j, vertexcount);
				vertexlist[i].add(t1);		// add the vertex in the list of both the lines set
				vertexlist[j].add(t1);
				totalvertex.add(t1);		// set of all vertices
				++vertexcount;
			}
		}		
		
		mycomp = new Comparator<Vertex>(){
			public int compare(Vertex v1, Vertex v2){
	            return  v1.x-v2.x;
	        }
		};
		
		int edgecount = 0;
		for(int i=0;i<Graph.numlines;++i){
			Collections.sort(vertexlist[i], mycomp);	// sort all the vertices for each line
			for(int j=0;j<vertexlist[i].size()-1;++j){				
				Edge e1 = new Edge(vertexlist[i].get(j).id, vertexlist[i].get(j+1).id, edgecount);
				System.out.println("EDGE: V1 = "+e1.v1+"  V2 = "+e1.v2);
				totaledge.add(e1);
				++edgecount;
			}
		}	
		
		numedges = totaledge.size();
		numnodes = totalvertex.size();		
		return;
	}
}
