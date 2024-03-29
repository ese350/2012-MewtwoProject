package org.OpenNI.Samples.AngleTracker;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.*;
// for future consideration : add JOption Pane to prompt for number of repetitions for each movement 
// add JLabel for choosing what limb 
// multiplayer 
@SuppressWarnings("serial")
public class Arena extends JComponent {
	// place all game objects that need to be moved except for turtle
	private Map<String, GameObject> movingObjects = new HashMap<String, GameObject>();
	private Map<Integer, String> rando = new HashMap<Integer, String>();
	private Map<String, GameObject> stationaryObjects = new HashMap<String, GameObject>();
	private boolean j = false;
	private int lasty = 0; // y value before jumping 
	private int counter = 0; // for invincibility 
	private boolean isJumpingShortHurdle = false; // if turtle is in the process of jumping regular hurdle
	private boolean isJumpingTallHurdle = false;
	private boolean lose; // lost the game
	private int lives = 5; // number of lives
	private boolean pause = false; // when pausing the game 
	private boolean pressed = false; // when space is pressed 
	private int distanceTravelled = 0; // keeps track of distance travelled 
	private int level = 1; // starting level of the game
	private int distanceToLevel = 0; //remaining distance before going on to next level 
	private boolean instruction = false; 
	private boolean up = false; 
	private int pixelCounter = 0; 
	private Turtle turtle;
	private int numMovingObject = 0; 
	private int randomKey = 0; // for randomly generating what obstacle to show up 
	private int numShortHurdles = 0; // number of short hurdles appeared
	private int numTallHurdles = 0; // number of tall hurdles appeared
	private int dieShortHurdle = 0; // how many times turtle did not jump over the short hurdle 
	private int dieTallHurdle = 0; // number of times turtle did 
	private int imageIndex = 1; // keeps track of which image to display when turtle is rotating during jump 
	
	static ImageIcon turtlelife = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtlelife.png");
			// image of turtle when not invincible 
	private static Image tl = turtlelife.getImage();
	
	static ImageIcon turtlereg = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/turtle1.png");
			// image of turtle when invincible 
	private static Image tr = turtlereg.getImage();
	
	static ImageIcon intro = 
			new ImageIcon("/home/chenst/MewtwoProject/Assets/intropage2.png");
			// image of intropage
	private static Image ip = intro.getImage();
  
	
	private int interval = 70; // Milliseconds between updates.
	private Timer timer;       // Each time timer fires we animate one step.

	final int COURTWIDTH  = 644; // width of the track 
	final int COURTHEIGHT = 145+145; // height of track + mountain 

	private int turtleV = 7;  // How fast does the turtle move 5 pixels / tick()
	private int obstacleV = 4; 
	private int distanceApart = 161;
	
	String strFilePath = "/home/chenst/test.txt";
	FileOutputStream fos;
	DataOutputStream dos;
	
	public Arena(AngleTracker app) {
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);

		timer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(instruction){
						// to display the instruction page
					pause = true; // then pause everything 
				}
				if(pause){} 
				else tick(); 
				}
			}); 
		// action performed every 100 milliseconds
		timer.start(); 
		   
		    try
		    {
		      //create FileOutputStream object
		      fos = new FileOutputStream(strFilePath);
		       dos = new DataOutputStream(fos);
		       
		    }
		    catch (IOException e)
		    {
		      System.out.println("IOException : " + e);
		    }
		 

		app.addListener(new AngleTracker.Listener() {
		      @Override
		      public void afterValueChanged(AngleTracker angNow) {
		    	  String val = angNow.getValue();
		    	  if (val.equals("jump") && !pressed){
		    		  jump();
		    		  try {
		    			  dos.writeChar(65);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	  }
		    	  else if (val.equals("hijump") && !pressed){
		    		  jumpHigher();
		    		  try {
		    			  dos.writeChar(65);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	  }
		    	  else {
					up = false;
					if(pause){
						pause = false;
					}
					if(pressed) // if pressed the space 
						pressed = false; // make false when released 
					else{ // if didn't press the space
						turtle.setVelocity(0, 0); 
					}
		    	  }
		    }});
		/*
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE){
						if(!pressed)
							jump(); 
				}
				else if (e.getKeyCode() == KeyEvent.VK_J){
						if(!pressed)
							jumpHigher(); 
				} 
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					turtle.setVelocityX(turtleV);
				else if (e.getKeyCode() == KeyEvent.VK_LEFT)
					turtle.setVelocityX(-turtleV);
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					up = true;
					turtle.setVelocityY(-turtleV);
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					//System.out.println("pressed down key");
					turtle.setVelocityY(turtleV);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if(!pause) // make movingObjects into data structure later (set)
						pause = true; // it is paused 			
					else
						pause = false; 
				}
				else if (e.getKeyCode() == KeyEvent.VK_S)
					instruction = false;
			}

			public void keyReleased(KeyEvent e) {
				up = false;
				//System.out.println("released");
				if(pause){
					pause = false;
				}
				if(pressed) // if pressed the space 
					pressed = false; // make false when released 
				else{ // if didn't press the space
					turtle.setVelocity(0, 0); 
				// velocity should be 0 when no key is pressed 
				// isJumpingHurdleHurdle = false; should be somewhere else 
				//System.out.println("isJumpingHurdleHurdle is false");
				}
			}
			
		});*/

	}

	/** Set the state of the state of the game to its initial value and
	    prepare the game for keyboard input. */
	public void reset() {
		stationaryObjects.put("track", new Track(0, 145));
		stationaryObjects.put("mountain", new Mountains(0,0));
		movingObjects.put("hurdle", new Hurdle(COURTWIDTH, COURTHEIGHT, -obstacleV)); 
		rando.put(numMovingObject,"hurdle");
		updateNumObject(); 
		movingObjects.put("tallhurdle", new TallHurdle(COURTWIDTH, COURTHEIGHT, -obstacleV));
		rando.put(numMovingObject,"tallhurdle");
		turtle = new Turtle(COURTWIDTH, COURTHEIGHT);
		
		requestFocusInWindow();
	}


   /** Update the game one timestep by moving the ball and the paddle. */
	void tick() { 
		pixelCounter = pixelCounter - 1; 
		/*try {
			dos.writeChar(65);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		lose = loseGame(); // check if the game's over & increase counter
		if(lose){ 
			lost(); 
		}
		
		levelUp(); // checks if accomplished 1 level
		
		moveObject();
		//isJumpingShortHurdle = turtle.land(lasty,isJumpingShortHurdle); 
		
		if(up){
			turtle.topClip(COURTWIDTH, 145);
		}
		else
			turtle.botClip(turtle.getRightBound(),turtle.getBottomBound());
		
		
		if (isJumpingShortHurdle)
			isJumpingShortHurdle = turtle.land(lasty,isJumpingShortHurdle); // land at the initial jumping y position when pressed spaced bar
		else if (isJumpingTallHurdle)
			isJumpingTallHurdle = turtle.land(lasty,isJumpingTallHurdle); 
			
		
		loseGame();
		repaint(); // Repaint indirectly calls paintComponent.
	}
	public boolean loseGame(){
		if(lives <= 0) {// condition for losing the game
			return true;
		}
		else { 
			counter = counter - 1; // give time before another hurdle can take away life 
			if(counter <= 0){ // when it not just touched a hurdle
				if (	   (turtle.intersects(movingObjects.get("hurdle")) && !(isJumpingShortHurdle || isJumpingTallHurdle))) {
					// add die sound here
					dieShortHurdle = dieShortHurdle + 1;
					lives = lives -1; // takes out a life
					counter = 100; // starts a counter that will run tick 20 times before another life can be lost *mention* in instruction 
				}
				else if ((turtle.intersects(movingObjects.get("tallhurdle")) && !isJumpingTallHurdle)
					// add die sound here
						){ 
					dieTallHurdle = dieTallHurdle + 1;
					lives = lives -1; // takes out a life
					counter = 100; // starts a counter that will run tick 20 times before another life can be lost *mention* in instruction 
					}
				}
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
		   g.drawString("You successfully did "+Math.max((numShortHurdles - dieShortHurdle), 0)+" regular repetitions", COURTWIDTH/2, COURTHEIGHT/2 + 40);
		   g.drawString("You successfully did "+Math.max((numTallHurdles - dieTallHurdle), 0)+" reach repetitions", COURTWIDTH/2, COURTHEIGHT/2 + 60);
		   
	   } 
	   else{
		super.paintComponent(g); // Paint background, border
		for (GameObject gameObject : stationaryObjects.values()){
			gameObject.draw(g);
		}
		movingObjects.get(rando.get(randomKey)).draw(g);
		if(isJumpingTallHurdle || isJumpingShortHurdle){
			imageIndex = Math.min(imageIndex+1, 16); 
			turtle.setImage(turtle.getPic(imageIndex));
			
		turtle.draw(g);
		}
		else {
			imageIndex = 1;
			if(counter <= 0){
				turtle.setImage(tr);
			}
			else{
				turtle.setImage(tl);
			}
			
			turtle.draw(g);
			} 	
		}
		g.drawString("Distance: "+distanceTravelled, 5, 10);
		g.drawString("Lives" +lives, 5, 20);
		g.drawString("Level: "+level, 5, 30);
		if(instruction)
	   		g.drawImage(ip,COURTWIDTH/2 - 267/2, COURTHEIGHT/2 - 173/2, null);
   }
   	
	   
	//}
   
   public void lost(){
	   pause = true;
   }
   
   public void jump(){
	 
	   // add jump sound here
	    isJumpingShortHurdle = true; 
	    lasty = turtle.y;
		if(!pressed){
			{
				while(!j){
					
					turtle.setVelocityY(-turtleV);
					tick();
					//repaint();

						if (turtle.y <= lasty - 90){
							j = true;
						}
					}
					while (!j){}
					turtle.setVelocityY(turtleV);
						tick();	
						j = false;				
			}
		pressed = true;
		}
   }
   
   public void jumpHigher(){
	   	// add jump sound here
	    isJumpingTallHurdle = true; 
	    lasty = turtle.y;
		if(!pressed){
			{
				while(!j){					
					turtle.setVelocityY(-turtleV);					
					tick();
						if (turtle.y <= lasty - 110){
							j = true;
						}
					}
					while (!j){}
						turtle.setVelocityY(turtleV);
						tick();	
						j = false;				
			}
		pressed = true;
		}
  }
	   

   // generates random obstacle and moves it in tick(); 
   // moves player 
   public void moveObject(){
	   int hurdleType = randomKey;
	   Random rnd = new Random();
	   //int randomKey; // starts off w/ first object 
	   if(pixelCounter <= 0){
		   randomKey = rnd.nextInt(numMovingObject + 1); // generates the key of next obstacle 
		   if (hurdleType == 0) // edit this if more stuff is added
 		   numShortHurdles = numShortHurdles + 1; 
		   else numTallHurdles = numTallHurdles + 1;
		   pixelCounter = distanceApart;
	   }
	   turtle.setBounds(getWidth(), getHeight());
	   movingObjects.get(rando.get(randomKey)).setBounds(getWidth(), getHeight());
	   turtle.move();
	   if(isJumpingShortHurdle || isJumpingTallHurdle){
	   }
	   movingObjects.get(rando.get(randomKey)).move();
	   movingObjects.get(rando.get(randomKey)).reenter();
	   distanceTravelled = distanceTravelled + 1;	   
   }
   public void levelUp(){
	   distanceToLevel = distanceToLevel +1;
	   if (distanceToLevel == 300){
		   level = level + 1;
		   distanceToLevel = 0;
		   obstacleV = level * 5;
	   }
   }
   
   public void updateNumObject(){
	   numMovingObject = numMovingObject + 1;
   }
   @Override
	public Dimension getPreferredSize() {
		return new Dimension(COURTWIDTH, COURTHEIGHT);
   }
}
