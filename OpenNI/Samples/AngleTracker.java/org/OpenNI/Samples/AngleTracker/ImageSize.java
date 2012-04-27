package org.OpenNI.Samples.AngleTracker;

import java.awt.*;
import java.awt.event.*;

public class ImageSize extends Frame{
  Image image;
  public static void main(String[] args) {
  try{
  new ImageSize();
  }
  catch(InterruptedException e){}
  }
  public ImageSize() throws InterruptedException{
  Toolkit tool = Toolkit.getDefaultToolkit();
  image = tool.getImage("/home/chenst/MewtwoProject/Assets/tallhurdle.png");
  MediaTracker mTracker = new MediaTracker(this);
  mTracker.addImage(image,1);
  mTracker.waitForID(1);
  int width = image.getWidth(null);
  int height = image.getHeight(null);
  System.out.println("The width of image: " + width);
  System.out.println("The height of image: " + height);
  setSize(width, height);
  setTitle("Image Size Example!");
  addWindowListener(new WindowAdapter(){
  public void windowClosing(WindowEvent we){
  System.exit(0);
  }
  });
  setVisible(true);
  }
  public void paint(Graphics g){
  g.drawImage(image,0,0,null);
  }
}