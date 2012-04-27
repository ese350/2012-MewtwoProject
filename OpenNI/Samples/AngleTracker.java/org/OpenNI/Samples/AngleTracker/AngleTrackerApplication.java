/****************************************************************************
*                                                                           *
*  OpenNI 1.x Alpha                                                         *
*  Copyright (C) 2011 PrimeSense Ltd.                                       *
*                                                                           *
*  This file is part of OpenNI.                                             *
*                                                                           *
*  OpenNI is free software: you can redistribute it and/or modify           *
*  it under the terms of the GNU Lesser General Public License as published *
*  by the Free Software Foundation, either version 3 of the License, or     *
*  (at your option) any later version.                                      *
*                                                                           *
*  OpenNI is distributed in the hope that it will be useful,                *
*  but WITHOUT ANY WARRANTY; without even the implied warranty of           *
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the             *
*  GNU Lesser General Public License for more details.                      *
*                                                                           *
*  You should have received a copy of the GNU Lesser General Public License *
*  along with OpenNI. If not, see <http://www.gnu.org/licenses/>.           *
*                                                                           *
****************************************************************************/
package org.OpenNI.Samples.AngleTracker;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class AngleTrackerApplication {

    /**
	 * 
	 */
	public AngleTracker viewer;
	private boolean shouldRun = true;
	private JFrame frame;

    public AngleTrackerApplication (JFrame frame)
    {
    	this.frame = frame;
    	frame.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					shouldRun = false;
				}
			}
		});
    }

    public static void main(String[] s)
    {
        JFrame f = new JFrame("OpenNI User Tracker");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        AngleTrackerApplication app = new AngleTrackerApplication(f);
        
        app.viewer = new AngleTracker();
        f.add("Center", app.viewer);
        f.pack();
        f.setVisible(true);
        
        /*final JFrame frame = new JFrame("Pong");
  	  	frame.setLocation(300, 300);

  	  	// Main playing area
  	  	final PongCourt court = new PongCourt(app.viewer);
  	  	frame.add(court, BorderLayout.CENTER);*/

        
        JFrame frame = new JFrame("Hurdle Turtle");
  	  	frame.setLocation(600, 400);

  	  	// Main playing area
  	  	Arena arena = new Arena(app.viewer);
  	  	frame.add(arena, BorderLayout.CENTER);
  	  	
  	  	frame.pack();
  	  	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	  	frame.setVisible(true);

  	  	// Start the game running
  	  	arena.reset();
  	  	
        MP3 mp3 = new MP3("/home/chenst/MewtwoProject/OpenNI/Samples/AngleTracker.java/org/OpenNI/Samples/AngleTracker/TMNT.mp3");
        mp3.play();
        //MP3Player mp3 = new MP3Player(s[0]);
        //mp3.run();
        
        app.run();
        
    }

    void run()
    {
        while(shouldRun) {
            viewer.updateDepth();
            viewer.repaint();
        }
        frame.dispose();
    }
    
}
