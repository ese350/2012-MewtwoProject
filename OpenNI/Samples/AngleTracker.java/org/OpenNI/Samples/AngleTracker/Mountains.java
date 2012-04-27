package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Mountains extends GameObject {
	final static int WIDTH = 958;
	final static int HEIGHT = 145;
	
	
	static ImageIcon m = new ImageIcon("/home/chenst/MewtwoProject/Assets/mountain.png");
	private static Image mountain = m.getImage();
	
	
	public Mountains(int x, int y) {
		super(x, y, 0, 0 , WIDTH, HEIGHT, mountain);
	}

	public void accelerate() {
	}
	
	public void reenter(){
		
	}

	public void draw(Graphics g) {
		//g.fillRect(x, y, WIDTH, HEIGHT);
		g.drawImage(img, x, y, null);
		//System.out.println("the width of hurdle is "+h.getIconWidth()+" the height is "+h.getIconHeight()+"");
	}
	
	public String getName(){
		return "mountain";
	}
}

