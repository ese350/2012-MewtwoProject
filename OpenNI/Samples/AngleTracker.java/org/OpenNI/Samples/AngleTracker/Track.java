package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Track extends GameObject {
	final static int WIDTH = 644;
	final static int HEIGHT = 145;
	
	
	static ImageIcon t = new ImageIcon("/home/chenst/track2.png");
	private static Image i = t.getImage();
	
	
	public Track(int x, int y) {
		super(x, y, 0, 0 , WIDTH, HEIGHT, i);
	}

	public void accelerate(int boundvalue) {
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