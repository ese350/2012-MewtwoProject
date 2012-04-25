package org.OpenNI.Samples.AngleTracker;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MP3Player extends Thread
{
   private String filename;
   private Player player; 
   private Thread mp3Thread;
   private boolean playing;
   private boolean locked;
    
   // constructor that takes the name of an MP3 file
   public MP3Player(String filename) 
   {
      this.filename = filename;
      mp3Thread = null;
      playing = true;
      locked = true;
   }
   
   public void run()
   {
      while (playing)
         play();
   }

   public void play() 
   {
      if (!locked)
      {
         if (filename != null && filename != "")
         {
            try
            {
               FileInputStream fis     = new FileInputStream(filename);
               BufferedInputStream bis = new BufferedInputStream(fis);
               player = new Player(bis);
               
               mp3Thread = new Thread() 
               {
                  public void run() 
                  { try { player.play(); } catch (Exception e) { System.out.println(e); } }
               };
            
               mp3Thread.start();
               mp3Thread.join();
            } catch (Exception e) { System.out.println(e); }
         }
      }
   }

   public void changeMusic(String newGuy)
   {
      locked = true;
      filename = "";
      try
      {
         if (player != null)
            player.close();
         mp3Thread = null;
         
         filename = ".\\music/" + newGuy + ".mp3";
         
         locked = false;
      }
      catch (Exception e) {}
   }
   
   public void close() 
   {
      locked = true;
      playing = false; 
      
      try 
      { 
         if (player != null) 
            player.close(); 
         
         if (mp3Thread != null)
            mp3Thread = null;
      } catch (Exception e) {}
   }
   
}