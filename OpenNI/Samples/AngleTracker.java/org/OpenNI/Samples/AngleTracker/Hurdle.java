package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Hurdle extends GameObject {
	final static int WIDTH = 26;
	final static int HEIGHT = 160;
	
	
	static ImageIcon h = new ImageIcon("/home/chenst/MewtwoProject/Assets/hurdle2.png");
	private static Image i = h.getImage();
	
	
	public Hurdle(int x, int y, int velocityX) {
		super(x, y - HEIGHT, velocityX, 0 , WIDTH, HEIGHT, i);
	}

	public void accelerate() {
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
		return "hurdle";
	}

}