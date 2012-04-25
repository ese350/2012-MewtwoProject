package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Wall extends GameObject {
	final static int WIDTH = 25;
	final static int HEIGHT = 98;
	
	
	static ImageIcon wall = new ImageIcon("/home/chenst/wall2.png");
	private static Image i = wall.getImage();
	
	
	public Wall(int x, int y, int velocityX) {
		super(x, y - HEIGHT, velocityX, 0 , WIDTH, HEIGHT, i);
	}	public void accelerate(int boundvalue) {
	}
	public void reenter() { // when hurdle moves off the pages, renters from the right side
		if (x <= 0)
			x = rightBound;
	}
	

	// Bounce the ball, if necessary
	/*public void bounce(Intersection i) {
		switch (i) {
		case NONE: break;
		case UP: velocityY = -Math.abs(velocityY); break;
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

