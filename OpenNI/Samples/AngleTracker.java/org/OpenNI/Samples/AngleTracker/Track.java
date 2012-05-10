package org.OpenNI.Samples.AngleTracker;


import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Track extends GameObject {
	final static int WIDTH = 644;
	final static int HEIGHT = 145;
	
	
	static ImageIcon t = new ImageIcon("/home/chenst/MewtwoProject/Assets/track2.png");
	private static Image i = t.getImage();
	
	
	public Track(int x, int y) {
		super(x, y, 0, 0 , WIDTH, HEIGHT, i);
	}

	public void accelerate() {
	}

public void reenter(){
		
	}
	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}
public String getName(){
	return "track";
}

}