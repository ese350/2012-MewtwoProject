package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Wall extends GameObject {
	final static int WIDTH = 25;
	final static int HEIGHT = 98;
	
	
	static ImageIcon wall = new ImageIcon("/home/chenst/MewtwoProject/Assets/wall2.png");
	private static Image i = wall.getImage();
	
	
	public Wall(int x, int y, int velocityX) {
		super(x, y - HEIGHT, velocityX, 0 , WIDTH, HEIGHT, i);
	}	public void accelerate() {
	}
	public void reenter() { // when hurdle moves off the pages, renters from the right side
		if (x <= 0)
			x = rightBound;
	}


	public void draw(Graphics g) {
		//g.fillRect(x, y, WIDTH, HEIGHT);
		g.drawImage(img, x, y, null);
		//System.out.println("the width of hurdle is "+h.getIconWidth()+" the height is "+h.getIconHeight()+"");
	}
	
	public String getName(){
		return "wall"; // could be left or right wall 
	}

}

