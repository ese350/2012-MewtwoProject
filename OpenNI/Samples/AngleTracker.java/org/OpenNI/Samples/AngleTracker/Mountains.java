package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Mountains extends GameObject {
	final static int WIDTH = 958;
	final static int HEIGHT = 145;
	
	
	static ImageIcon mountain = new ImageIcon("C:/Users/Amy/Desktop/Spring 2012/CIS120/mountain.PNG");
	private static Image i = mountain.getImage();
	
	
	public Mountains(int x, int y, int velocityX) {
		super(x, 0, velocityX, 0 , WIDTH, HEIGHT, i);
	}

	public void accelerate(int boundvalue) {
	}
	public void reenter() { // when hurdle moves off the pages, renters from the right side
		if (x <= 0)
			x = rightBound;
	}
	

	// Bounce the ball, if necessary
	/*public void bounce(Intersection i) {
		switch (i) {
		case NONE: break;
		case UP: velocityY = -Math.abs(velocityY); break ;
		case DOWN: velocityY = Math.abs(velocityY); break;
		case LEFT: velocityX = -Math.abs(velocityX); break;
		case RIGHT: velocityX = Math.abs(velocityX); break;
		}
	}
	*/

	public void draw(Graphics g) {
		//g.fillRect(x, y, WIDTH, HEIGHT);
		g.drawImage(img, x, y, null);
		//System.out.println("the width of hurdle is "+h.getIconWidth()+" the height is "+h.getIconHeight()+"");
	}

}
