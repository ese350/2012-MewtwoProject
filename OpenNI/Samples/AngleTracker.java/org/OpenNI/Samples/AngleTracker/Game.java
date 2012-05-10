package org.OpenNI.Samples.AngleTracker;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Game implements Runnable {
   public void run() {
      // Top-level frame
      final JFrame frame = new JFrame("Hurdle Turtle");
      frame.setLocation(300, 300);

      // Main playing area
      final Arena arena = new Arena(new AngleTracker());
      frame.add(arena, BorderLayout.CENTER); // add what, where

      // Reset button
 
      final JPanel panel = new JPanel();
      frame.add(panel, BorderLayout.NORTH);
      final JButton reset = new JButton("Reset");
      reset.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            arena.reset();
         }
      });
      panel.add(reset);
      // Put the frame on the screen
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

      // Start the game running
      arena.reset();
      }

   /*
    * Get the game started!
    */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(new Game());
   }

}
