package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class Turtle extends GameObject {
	final static int HEIGHT = 37; // height of the object, need to getWidth()
	final static int WIDTH = 48;
	private int ah;
	private Map<Integer, Image> turtlePics = new HashMap<Integer, Image>();
	
	static ImageIcon one = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle2.png");
	private static Image i1 = one.getImage();
	
	
	static ImageIcon two = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle4.png");
	private static Image i2 = two.getImage();
	
	static ImageIcon three = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle7.png");
	private static Image i3 = three.getImage();
	
	static ImageIcon four = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle9.png");
	private static Image i4 = four.getImage();
	
	static ImageIcon five = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle10.png");
	private static Image i5 = five.getImage();
	
	static ImageIcon six = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle12.png");
	private static Image i6 = six.getImage();
	
	static ImageIcon seven = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle14.png");
	private static Image i7 = seven.getImage();
	
	static ImageIcon eight = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle15.png");
	private static Image i8 = eight.getImage();
	
	static ImageIcon nine = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle16.png");
	private static Image i9 = nine.getImage();
	
	static ImageIcon ten = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle17.png");
	private static Image i10 = ten.getImage();
	
	static ImageIcon eleven = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle18.png");
	private static Image i11 = eleven.getImage();
	
	static ImageIcon twelve = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle19.png");
	private static Image i12 = twelve.getImage();
	
	static ImageIcon thirteen = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle20.png");
	private static Image i13 = thirteen.getImage();
	
	static ImageIcon fourteen = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle21.png");
	private static Image i14 = thirteen.getImage();
	
	static ImageIcon fifteen = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle22.png");
	private static Image i15 = thirteen.getImage();
	
	static ImageIcon sixteen = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle23.png");
	private static Image i16 = thirteen.getImage();
	
	public Turtle(int arenawidth, int arenaheight) {
		super((arenawidth - WIDTH) / 2, 
				(arenaheight - HEIGHT) / 2, 0, 0, WIDTH, HEIGHT, i1);
		ah = arenaheight;
		turtlePics.put(1, i1);
		turtlePics.put(2, i2);
		turtlePics.put(3, i3);
		turtlePics.put(4, i4);
		turtlePics.put(5, i5);
		turtlePics.put(6, i6);
		turtlePics.put(7, i7);
		turtlePics.put(8, i8);
		turtlePics.put(9, i9);
		turtlePics.put(10, i10);
		turtlePics.put(11, i11);
		turtlePics.put(12, i12);
		turtlePics.put(13, i13);
		turtlePics.put(14, i14);
		turtlePics.put(15, i15);
		turtlePics.put(16, i16);
	} 
	
	
	public int initialHeight(){
		return ah;
	}
	
	public boolean land(int bound, boolean jumped){
		if(jumped){
			if(y + 7 > bound){
				velocityY = 0;
				return false; // after landing, isJumping should be false
			}
			else return true; // still need to be in the air 
		}
		return false; // if it's not jumping, it's still not jumping 
	}
	public void accelerate() {
		if (x < 0 || x > rightBound)
			velocityX = 0;
		if (y > (ah - HEIGHT)){
			velocityY = 0;
		}
		if (y < 0)
			velocityY = 10; 
	}
	
	public Image getPic(int i){
		return turtlePics.get(i);
	}
public void reenter(){
		
	}
	 
	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}
	
	public String getName(){
		return "turtle";
	}
}



