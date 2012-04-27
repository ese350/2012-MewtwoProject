package org.OpenNI.Samples.AngleTracker;

import java.awt.Graphics;
import java.awt.Image;

public abstract class GameObject {
	int x; // x and y coordinates upper left
	int y;

	int width;
	int height;

	int velocityX; // Pixels to move each time move() is called.
	int velocityY;
	
	int lastvx;
	int lastvy; 
	
	int rightBound; // Maximum permissible x, y values.
	int bottomBound;
	Image img; // image of the object at the time

	public GameObject(int x, int y, int velocityX, int velocityY, int width,
			int height, Image i ) {
		this.x = x; // position of the object from upper left corner
		this.y = y;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.width = width; // setting input width = width
		this.height = height;
		this.img = i;
		this.lastvx = 0;
		this.lastvy = 0; 
	}

	public void setBounds(int width, int height) {
		rightBound = width - this.width; // rightBound = the width of arena - width of the image gets x coord
		bottomBound = height - this.height;
	}
	public int getRightBound(){
		return rightBound;
	}
	public int getBottomBound(){
		return bottomBound;
	}
	
	public void setVelocityX(int velocityX){
		this.velocityX = velocityX;
	}
	
	public void setVelocityY(int velocityY){
		this.velocityY = velocityY;
	}
	
	public void setVelocity(int velocityX, int velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	// Move the object at the given velocity.
	public void move() {
		x += velocityX;
		y += velocityY;
		

		//accelerate(); // what does this do?
		//clip(); // what does this do?
	}

	// Keep the object in the bounds of the court
	public void botClip(int rb, int bb) {
		if (x < 0) // if object is less than the arena left bound, object stay there
			x = 0;
		else if (x > rb) // if object is greater than the inputed rightbound, object stays at rightbound
			x = rb;
		if (y < 0) // if object is above the arena's top bound, object stays there
			y = 0;
		else if (y > bb){ // if object is lower than bottombound, object stays there
			y = bb;
			//System.out.println("clipped with y at "+y); 
		}
	}
	
	public void topClip(int rb, int tb){
		if (x < 0)
			x = 0;
		else if (x > rb)
			x = rb;
		if (y < tb)
			y = tb;	
	}

// Compute whether the two objects touch, use for walls 
	public boolean touch(GameObject other){
		if( 	   x + width == other.x
				|| y + height == other.y
				|| x == other.width + other.x
				|| y == other.height + other.y){
	//		System.out.println("touched");
		//	System.out.println("turtle touched at x direction : "+(x + width));
			//System.out.println("wall touched at x direction : "+(other.x));
			//System.out.println("turtle touched at y direction : "+(y + height));
			//System.out.println("wall touched at x direction : "+(other.y));
			return true;
		}
		else return false;
	}
	
	/**
	 * Compute whether two GameObjects intersect.
	 *
	 * @param other
	 *            The other game object to test for intersection with.
	 * @return NONE if the objects do not intersect. Otherwise, a direction
	 *         (relative to <code>this</code>) which points towards the other
	 *         object.
	 */
	public boolean intersects(GameObject other) {
		//System.out.println(other.getName()+"'s x is: "+x+ " width is: "+width);
		if (       other.x > x + width
				|| other.y > y + height
				|| other.x + other.width  < x
				|| other.y + other.height < y)
			return false;
		else {
		//	System.out.println("intersected");
			return true;
		}
		// compute the vector from the center of this object to the center of
		// the other
		/*double dx = other.x + other.width /2 - (x + width /2);
		double dy = other.y + other.height/2 - (y + height/2);

		double theta = Math.atan2(dy, dx);
		double diagTheta = Math.atan2(height, width);

		if ( -diagTheta <= theta && theta <= diagTheta )
			return Intersection.RIGHT;
		if ( diagTheta <= theta && theta <= Math.PI - diagTheta )
			return Intersection.DOWN;
		if ( Math.PI - diagTheta <= theta || theta <= diagTheta - Math.PI )
			return Intersection.LEFT;
		// if ( diagTheta - Math.PI <= theta && theta <= diagTheta)
		return Intersection.UP; */
	}
	public abstract void reenter();
	public abstract void accelerate();
	public boolean land(int bound, boolean jumped){
		if(jumped){
			if(y > bound){
				velocityY = 0;
			//	System.out.println("in land(), landed at: "+y+
					//	" with velocity y: "+velocityY);
				return false;
			}
			else return true;
		}
		return false;
	}

	public abstract void draw(Graphics g);
	
	
/*	public void pause(){
		lastvx = velocityX; // stores last velocity before pauses 
		lastvy = velocityY; 
		velocityX = 0; 
		velocityY = 0; 
	} */
	
	public void setImage(Image i){
		this.img = i;
	}
	
	public abstract String getName();
}

