package org.OpenNI.Samples.AngleTracker;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Arena extends JComponent {
	private Turtle turtle;
	private Hurdle hurdle;
	private Hurdle hurdle2;
//	private Track track;
	private boolean lose;
	private int lives = 3;
	private boolean pause;
	private boolean pressed = false;
	//private Image t; // image of the turtle
	private int distanceTravelled = 0;
	private Track track;
	private Wall wallright;
	private Wall wallleft;
	private Mountains mountainfirst;
	private Mountains mountainsecond;
	private boolean j = false;
	private int lasty = 0;
	private int counter = 0;
	private boolean touched = false; 
	private boolean isJumping = false; 
	
	//ImageIcon s = new ImageIcon("/home/amy/CIS120/MyGame/turtle.jpg"); // image of the turtle when it's just running
	//ImageIcon tr = new ImageIcon("C:/Users/Amy/Desktop/Spring 2012/CIS120/track.PNG");
	
	//ImageIcon test = new ImageIcon(tr.getImage());
  
	
	private int interval = 100; // Milliseconds between updates.
	private Timer timer;       // Each time timer fires we animate one step.

	final int COURTWIDTH  = 644;
	final int COURTHEIGHT = 145+145;

	final int TURTLE_VEL  = 5;  // How fast does the turtle move
	
	
	public Arena(AngleTracker app) {
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);

		timer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) { tick(); }});
		timer.start(); // what does tick() do?

		app.addListener(new AngleTracker.Listener() {
		      @Override
		      public void afterValueChanged(AngleTracker angNow) {
		    	  if (angNow.getValue()){
		    		  jump();
		    	  }
		    	  else {
		    		  if(pressed)
		    		  {
							pressed = false;
		    		  }
						if(!pressed){
						turtle.setVelocity(0, 0);
						isJumping = false;
						}
		    	  }
		    }});
		/*
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE){
				    jump();
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					//System.out.println("jumped");
					turtle.setVelocityX(TURTLE_VEL);
				else if (e.getKeyCode() == KeyEvent.VK_LEFT)
					turtle.setVelocityX(-TURTLE_VEL);
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					turtle.setVelocityY(-TURTLE_VEL);
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					turtle.setVelocityY(TURTLE_VEL);
				else if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if(!pause){ // make gameobjects into data structure later (set)
					turtle.pause(); 
					hurdle.pause();
					hurdle2.pause(); 
					pause = true;
					}
					else{
					turtle.setVelocity(turtle.lastvx, turtle.lastvy);	
					hurdle.setVelocity(hurdle.lastvx, hurdle.lastvy);
					pause = false;
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_R)
					reset();
			}

			public void keyReleased(KeyEvent e) {
				//System.out.println("released");
				if(pressed)
					pressed = false;
				if(!pressed){
				turtle.setVelocity(0, 0);
				isJumping = false;
				System.out.println("isJumping is false");
				}// could make boolean to determine whether jumped
			}
			
		});*/
		// After a PongCourt object is built and installed in a container
		// hierarchy, somebody should invoke reset() to get things started...
	}

	/** Set the state of the state of the game to its initial value and
	    prepare the game for keyboard input. */
	public void reset() {
		//t = s.getImage();
		//turtle = new Turtle(0, 0, 2, 3, );
		track = new Track(0, 145);
	//	mountainfirst = new Mountains(0,0,-2);
		//mountainsecond = new Mountains(COURTWIDTH,0,-2);
		hurdle = new Hurdle(COURTWIDTH, COURTHEIGHT, -10);
	//	if (distanceTravelled == 227){
		hurdle2 = new Hurdle((COURTWIDTH *3)/2, COURTHEIGHT, -10);
	//	}
	//	wallright = new Wall(COURTWIDTH + 250, COURTHEIGHT, -2);
	//	wallleft = new Wall(COURTWIDTH + 750, COURTHEIGHT - (wallright.height/2) , -2);
		turtle = new Turtle(COURTWIDTH, COURTHEIGHT);
		requestFocusInWindow();
	}

   /** Update the game one timestep by moving the ball and the paddle. */
	void tick() { 
		lose = loseGame();
		if(lose){ // check if already lost; 
			//System.out.println("lose is true");
			lost();
		}
		
		if (!pause){ //&& !lose
		distanceTravelled = distanceTravelled + 1;
		}
		hurdle.setBounds(getWidth(), getHeight()); // getWidth() gets the width of the arena = 3
		hurdle2.setBounds(getWidth(), getHeight()); // getWidth() gets the width of the arena = 3
	//	wallright.setBounds(getWidth(), getHeight());
		//wallleft.setBounds(getWidth(), getHeight());
		turtle.setBounds(getWidth(), getHeight());
		//mountainfirst.setBounds(getWidth(), getHeight());
		//mountainsecond.setBounds(getWidth(), getHeight());
		hurdle.move();
		hurdle2.move();
		//mountainfirst.move();
		//mountainsecond.move();
	//	wallright.move();
	//	wallleft.move();
		hurdle.reenter();
		hurdle2.reenter();
		//mountainfirst.reenter();
		//mountainsecond.reenter();
	//	wallright.reenter();
	//	wallleft.reenter();
		//hurdle.accelerate();
		//hurdle2.accelerate();
	//	mountainfirst.accelerate();
		//mountainsecond.accelerate();
		//wallright.accelerate();
		//wallleft.accelerate();
		turtle.setBounds(getWidth(), getHeight());
		turtle.land(lasty,isJumping); // land at the initial jumping y position when pressed spaced bar
		//System.out.println("last y after landing: "+lasty);
		turtle.move();
		//System.out.println("after move() y is "+turtle.y+", y velocity is "+turtle.velocityY);
		turtle.clip(turtle.getRightBound(),turtle.getBottomBound());
		loseGame();
		/*
		if (turtle.intersects(hurdle) || turtle.intersects(hurdle2) || 
				turtle.intersects(wallright) || turtle.intersects(wallleft)){
			lives = lives - 1;
			if (lives <= 0){
				lose = true;
				System.out.println("Lost"+lives);
			}
		} */
		/*if (distanceTravelled >= 227){
			hurdle2.setBounds(getWidth(), getHeight()); // getWidth() gets the width of the arena = 3
			hurdle2.move();
			hurdle2.reenter();
			hurdle2.accelerate();
			if (turtle.intersects(hurdle2)){
				lives = lives - 1;
				if (lives <= 0){
					lose = true;
					System.out.println("Lost"+lives);
				}
			}
		} */
		//lose = turtle.intersects(hurdle);
		//hurdle.bounce(turtle.intersects(hurdle));
		repaint(); // Repaint indirectly calls paintComponent.
	}
	public boolean loseGame(){
		if(lives <= 0) {// condition for losing the game
			//System.out.print("lost with lives: "+lives);
			return true;
		}
		else { 
			counter = counter - 1; // give time before another hurdle can take away life 
			//System.out.println("counter is: "+counter);
			if(counter <= 0){ // when it not just touched a hurdle
				if ((turtle.intersects(hurdle) && !isJumping || (turtle.intersects(hurdle2) && !isJumping))){
					lives = lives -1; // takes out a life
					//System.out.println("touched again");
					counter = 200; // starts a counter that will run tick 20 times before another life can be lost *mention* in instruction 
					}
				}
			//System.out.println("still alive with lives: "+lives);
			return false;
		}
	}
	public int getDistance(){
		return distanceTravelled;
	}
   @Override
	public void paintComponent(Graphics g) {
	  if(lose){ 
		   g.drawString("You Lost!", COURTWIDTH/2, COURTHEIGHT/2);
		   g.drawString("Distance Travelled: "+distanceTravelled,COURTWIDTH/2, COURTHEIGHT/2 + 20);
	   } 
	   //else{
		super.paintComponent(g); // Paint background, border
		track.draw(g);
		//mountainfirst.draw(g);
		//mountainsecond.draw(g);
		hurdle.draw(g);
		hurdle2.draw(g);
		turtle.draw(g);
		//System.out.println("drawn with velocity: " +turtle.velocityY+ ", j is"+j);
		//if(distanceTravelled >= 227){	
	//	wallright.draw(g);
	//	wallleft.draw(g);
	//	}
		g.drawString("Distance: "+distanceTravelled, 5, 10);
		g.drawString("Lives" +lives, 5, 20);
	   }
	//}
   
   public void lost(){
	   turtle.pause(); // pause method 
	   hurdle.pause();   
	   hurdle2.pause();
	   track.pause();
   }
   
   public void jump(){
	   isJumping = true;
	    lasty = turtle.y;
	    //System.out.println("lasty is stored as: "+lasty);
		//System.out.println("pressed");
		if(!pressed){
			{
				while(!j){
					
					turtle.setVelocityY(-TURTLE_VEL);
					tick();
					//repaint();
					
					//System.out.println("drawn in key with velocity: " +turtle.velocityY);
					//turtle.move();
					//System.out.println("velocityY is in keypress "+turtle.velocityY);
				 	//System.out.println("before getting above right height 100");
						if (turtle.y <=100){
							j = true;
							//System.out.println("got above height 100");
						}
					}
					while (!j){}
						turtle.setVelocityY(TURTLE_VEL);
						//turtle.clip(turtle.getRightBound(), lasty);
						tick();	
						j = false;
							/*System.out.println("also here");	
							if(turtle.y >= lasty){
								turtle.setVelocityY(0);
								j = false; 
							
					} */
			}
		pressed = true;
		}
   }
   
   @Override
	public Dimension getPreferredSize() {
		return new Dimension(COURTWIDTH, COURTHEIGHT);
   }
}
