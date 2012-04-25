package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Turtle extends GameObject {
	final static int HEIGHT = 37; // height of the object, need to getWidth()
	final static int WIDTH = 48;
	//final static int DIAMETER  = 10;
	//final int ARENAWIDTH;
	private int ah;
	private boolean j = false;
	//private boolean done = false; 
	static ImageIcon turtle = new ImageIcon("/home/chenst/turtle2.png");
	private static Image i = turtle.getImage();
	
	public Turtle(int arenawidth, int arenaheight) {
		super((arenawidth - WIDTH) / 2, (arenaheight - HEIGHT)/2, 0, 0, WIDTH, HEIGHT, i);
		//ARENAWIDTH = arenawidth;
		ah = arenaheight;
	} //
	
	
	public int initialHeight(){
		return ah;
	}
	
	public void land(int bound, boolean jumped){
		if(jumped){
		if(y > bound){
			velocityY = 0;
			//System.out.println("in land(), landed at: "+y+" with velocity y: "+velocityY);
		}
		}
	}
	public void accelerate(int boundvalue) {
		if (x < 0 || x > rightBound)
			velocityX = 0;
		if (y > (ah - HEIGHT)){
			velocityY = 0;
			//System.out.println("into accelerate with y :"+y);
		}
		if (y < 0)
			velocityY = 10; 
	}
	
	 

	public void draw(Graphics g) {
		//g.fillOval(x, y, WIDTH, HEIGHT);
		g.drawImage(img, x, y, null);
	}
}

//TMNT Theme - Metal Version [Instrumental]

