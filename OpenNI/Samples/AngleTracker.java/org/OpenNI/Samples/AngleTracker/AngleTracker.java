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

import org.OpenNI.*;

import java.util.Random;
import java.util.Scanner;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

public class AngleTracker extends Component
{
	class NewUserObserver implements IObserver<UserEventArgs>
	{
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args)
		{
			System.out.println("New user " + args.getId());
			try
			{
				if (skeletonCap.needPoseForCalibration())
				{
					poseDetectionCap.startPoseDetection(calibPose, args.getId());
				}
				else
				{
					skeletonCap.requestSkeletonCalibration(args.getId(), true);
				}
			} catch (StatusException e)
			{
				e.printStackTrace();
			}
		}
	}
	class LostUserObserver implements IObserver<UserEventArgs>
	{
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args)
		{
			System.out.println("Lost user " + args.getId());
			joints.remove(args.getId());
		}
	}
	
	class CalibrationCompleteObserver implements IObserver<CalibrationProgressEventArgs>
	{
		@Override
		public void update(IObservable<CalibrationProgressEventArgs> observable,
				CalibrationProgressEventArgs args)
		{
			System.out.println("Calibration complete: " + args.getStatus());
			try
			{
			if (args.getStatus() == CalibrationProgressStatus.OK)
			{
				System.out.println("starting tracking "  +args.getUser());
					skeletonCap.startTracking(args.getUser());
	                joints.put(new Integer(args.getUser()), new HashMap<SkeletonJoint, SkeletonJointPosition>());
			}
			else if (args.getStatus() != CalibrationProgressStatus.MANUAL_ABORT)
			{
				if (skeletonCap.needPoseForCalibration())
				{
					poseDetectionCap.startPoseDetection(calibPose, args.getUser());
				}
				else
				{
					skeletonCap.requestSkeletonCalibration(args.getUser(), true);
				}
			}
			} catch (StatusException e)
			{
				e.printStackTrace();
			}
		}
	}
	class PoseDetectedObserver implements IObserver<PoseDetectionEventArgs>
	{
		@Override
		public void update(IObservable<PoseDetectionEventArgs> observable,
				PoseDetectionEventArgs args)
		{
			System.out.println("Pose " + args.getPose() + " detected for " + args.getUser());
			try
			{
				poseDetectionCap.stopPoseDetection(args.getUser());
				skeletonCap.requestSkeletonCalibration(args.getUser(), true);
			} catch (StatusException e)
			{
				e.printStackTrace();
			}
		}
	}
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutArg<ScriptNode> scriptNode;
    private Context context;
    private DepthGenerator depthGen;
    private UserGenerator userGen;
    private SkeletonCapability skeletonCap;
    private PoseDetectionCapability poseDetectionCap;
    private byte[] imgbytes;
    private float histogram[];
    String calibPose = null;
    HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> joints;

    private boolean drawBackground = true;
    private boolean drawPixels = true;
    private boolean drawSkeleton = true;
    private boolean printID = true;
    private boolean printState = true;
    
    private double goal;
    private int reps;
    private int rep = 0;
    private int sets;
    private int set = 0;
    private long lastEventTime = 0;
    private long rightNow;
    private long timeSinceLast = 0;
    private boolean enoughElapsedTime;
    private boolean possibleBoredom = false;
    private Random rand = new Random();
    private boolean didAction = false;
    private boolean returnedToStart = true;

	SkeletonJoint midJoint;
	SkeletonJoint anchorJoint;
	SkeletonJoint movingJoint;
    
    private static String[] encPhrases = {"You can do it!", "One more!", 
    	"Almost there!", "Can’t is NOT a word!", 
    	"You can if you think you can!", "Pain is weakness leaving the body!"
    	};
    
    private static String[] slowPhrases = {"Don't give up now!", "Keep at it!"
    	};
    
    private BufferedImage bimg;
    int width, height;
    
    private final String SAMPLE_XML_FILE = "../../../../Data/SamplesConfig.xml";
    public AngleTracker()
    {

        try {
            scriptNode = new OutArg<ScriptNode>();
            context = Context.createFromXmlFile(SAMPLE_XML_FILE, scriptNode);

            depthGen = DepthGenerator.create(context);
            DepthMetaData depthMD = depthGen.getMetaData();

            histogram = new float[10000];
            width = depthMD.getFullXRes();
            height = depthMD.getFullYRes();
            
            imgbytes = new byte[width*height*3];

            userGen = UserGenerator.create(context);
            skeletonCap = userGen.getSkeletonCapability();
            poseDetectionCap = userGen.getPoseDetectionCapability();
            
            userGen.getNewUserEvent().addObserver(new NewUserObserver());
            userGen.getLostUserEvent().addObserver(new LostUserObserver());
            skeletonCap.getCalibrationCompleteEvent().addObserver(new CalibrationCompleteObserver());
            poseDetectionCap.getPoseDetectedEvent().addObserver(new PoseDetectedObserver());
            
            calibPose = skeletonCap.getSkeletonCalibrationPose();
            joints = new HashMap<Integer, HashMap<SkeletonJoint,SkeletonJointPosition>>();
            
            skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);
            
            System.out.println("\nWelcome to Rekibilitate!\n");
            // User input
        	
            boolean done = false;
            while (!done) {
	            try {
	            	Scanner stdin = new Scanner( System.in );
	            	System.out.println("What's your first name?");
	            	String name = stdin.next();
	            	
	            	if (!name.equals("Steven")){
	            		System.out.println("Looks like you're new. Welcome!");
	            	}
	            	else {
	            		System.out.println("Welcome back, " + name + "!");
	            	}
	            	
	            	System.out.println("What joint would you like to work today?");
	            	System.out.println("e.g. Right Knee");
	            	String direction = stdin.next();
	            	String joint = stdin.next();
	            	if (direction.equalsIgnoreCase("Left") && 
	            			joint.equalsIgnoreCase("Elbow")){
	            	    midJoint = SkeletonJoint.LEFT_ELBOW;
	            	   	anchorJoint = SkeletonJoint.LEFT_SHOULDER;
	            	   	movingJoint = SkeletonJoint.LEFT_HAND;
	            	}
	            	else if (direction.equalsIgnoreCase("Right") && 
	           				joint.equalsIgnoreCase("Elbow")){
	           	    	midJoint = SkeletonJoint.RIGHT_ELBOW;
	           	    	anchorJoint = SkeletonJoint.RIGHT_SHOULDER;
	           	    	movingJoint = SkeletonJoint.RIGHT_HAND;
	           		} 
	           		else if (direction.equalsIgnoreCase("Left") && 
	           				joint.equalsIgnoreCase("Knee")){
	           	    	midJoint = SkeletonJoint.LEFT_KNEE;
	           	    	anchorJoint = SkeletonJoint.LEFT_HIP;
	           	    	movingJoint = SkeletonJoint.LEFT_FOOT;
	           		} 
	           		else if (direction.equalsIgnoreCase("Right") && 
            				joint.equals("Knee")){
	            	    midJoint = SkeletonJoint.RIGHT_KNEE;
	            	   	anchorJoint = SkeletonJoint.RIGHT_HIP;
	            	   	movingJoint = SkeletonJoint.RIGHT_FOOT;
	            	} 
	            	else {
	            		System.out.println("Invalid input, please try again");
	           			continue;
	            	}
	            	
	            	System.out.println("Please enter your target angle:");
	            	goal = stdin.nextDouble();
	                System.out.println("Please enter number of reps:");
	                reps = stdin.nextInt();
	                System.out.println("Please enter number of sets:");
	                sets = stdin.nextInt();
	                System.out.println("Goal is: " + sets + " sets of " + reps +
	                		" reps at " + goal + " degrees");
	                System.out.println("Let's get started!");
	                done = true;
	            } catch (Exception e) {
	                System.err.println("Error:" + e.getMessage());
	                System.exit(1);
	            }
				Thread.sleep(1000);
				context.startGeneratingAll();
            }
        } catch (GeneralException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void calcHist(ShortBuffer depth)
    {
        // reset
        for (int i = 0; i < histogram.length; ++i)
            histogram[i] = 0;
        
        depth.rewind();

        int points = 0;
        while(depth.remaining() > 0)
        {
            short depthVal = depth.get();
            if (depthVal != 0)
            {
                histogram[depthVal]++;
                points++;
            }
        }
        
        for (int i = 1; i < histogram.length; i++)
        {
            histogram[i] += histogram[i-1];
        }

        if (points > 0)
        {
            for (int i = 1; i < histogram.length; i++)
            {
                histogram[i] = 1.0f - (histogram[i] / (float)points);
            }
        }
    }


    void updateDepth()
    {
        try {

            context.waitAnyUpdateAll();

            DepthMetaData depthMD = depthGen.getMetaData();
            SceneMetaData sceneMD = userGen.getUserPixels(0);

            ShortBuffer scene = sceneMD.getData().createShortBuffer();
            ShortBuffer depth = depthMD.getData().createShortBuffer();
            calcHist(depth);
            depth.rewind();
            
            while(depth.remaining() > 0)
            {
                int pos = depth.position();
                short pixel = depth.get();
                short user = scene.get();
                
        		imgbytes[3*pos] = 0;
        		imgbytes[3*pos+1] = 0;
        		imgbytes[3*pos+2] = 0;                	

                if (drawBackground || pixel != 0)
                {
                	int colorID = user % (colors.length-1);
                	if (user == 0)
                	{
                		colorID = colors.length-1;
                	}
                	if (pixel != 0)
                	{
                		float histValue = histogram[pixel];
                		imgbytes[3*pos] = (byte)(histValue*colors[colorID].getRed());
                		imgbytes[3*pos+1] = (byte)(histValue*colors[colorID].getGreen());
                		imgbytes[3*pos+2] = (byte)(histValue*colors[colorID].getBlue());
                	}
                }
            }
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }


    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    Color colors[] = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE};
    public void getJoint(int user, SkeletonJoint joint) throws StatusException
    {
        SkeletonJointPosition pos = skeletonCap.getSkeletonJointPosition(user, joint);
		if (pos.getPosition().getZ() != 0)
		{
			joints.get(user).put(joint, new SkeletonJointPosition(depthGen.convertRealWorldToProjective(pos.getPosition()), pos.getConfidence()));
		}
		else
		{
			joints.get(user).put(joint, new SkeletonJointPosition(new Point3D(), 0));
		}
    }
    public void getJoints(int user) throws StatusException
    {
    	getJoint(user, SkeletonJoint.HEAD);
    	getJoint(user, SkeletonJoint.NECK);
    	
    	getJoint(user, SkeletonJoint.LEFT_SHOULDER);
    	getJoint(user, SkeletonJoint.LEFT_ELBOW);
    	getJoint(user, SkeletonJoint.LEFT_HAND);

    	getJoint(user, SkeletonJoint.RIGHT_SHOULDER);
    	getJoint(user, SkeletonJoint.RIGHT_ELBOW);
    	getJoint(user, SkeletonJoint.RIGHT_HAND);

    	getJoint(user, SkeletonJoint.TORSO);

    	getJoint(user, SkeletonJoint.LEFT_HIP);
        getJoint(user, SkeletonJoint.LEFT_KNEE);
        getJoint(user, SkeletonJoint.LEFT_FOOT);

    	getJoint(user, SkeletonJoint.RIGHT_HIP);
        getJoint(user, SkeletonJoint.RIGHT_KNEE);
        getJoint(user, SkeletonJoint.RIGHT_FOOT);

    }
    void drawLine(Graphics g, HashMap<SkeletonJoint, SkeletonJointPosition> jointHash, SkeletonJoint joint1, SkeletonJoint joint2)
    {
		Point3D pos1 = jointHash.get(joint1).getPosition();
		Point3D pos2 = jointHash.get(joint2).getPosition();

		if (jointHash.get(joint1).getConfidence() == 0 || jointHash.get(joint2).getConfidence() == 0)
			return;

		g.drawLine((int)pos1.getX(), (int)pos1.getY(), (int)pos2.getX(), (int)pos2.getY());
    }
    
    public void drawSkeleton(Graphics g, int user) throws StatusException
    {
    	getJoints(user);
    	HashMap<SkeletonJoint, SkeletonJointPosition> dict = joints.get(new Integer(user));

    	outputAngle(user);

    	drawLine(g, dict, SkeletonJoint.HEAD, SkeletonJoint.NECK);

    	drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
    	drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);

    	drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
    	drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.LEFT_ELBOW);
    	drawLine(g, dict, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);

    	drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
    	drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.RIGHT_ELBOW);
    	drawLine(g, dict, SkeletonJoint.RIGHT_ELBOW, SkeletonJoint.RIGHT_HAND);

    	drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.TORSO);
    	drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.TORSO);
    	drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.RIGHT_HIP);

    	drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.LEFT_KNEE);
    	drawLine(g, dict, SkeletonJoint.LEFT_KNEE, SkeletonJoint.LEFT_FOOT);

    	drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.RIGHT_KNEE);
    	drawLine(g, dict, SkeletonJoint.RIGHT_KNEE, SkeletonJoint.RIGHT_FOOT);

    }
    
    public void outputAngle(int user)
    {
    	HashMap<SkeletonJoint, SkeletonJointPosition> dict = joints.get(new Integer(user));
    	
    	if (dict.get(midJoint).getConfidence() != 0 || 
    			dict.get(anchorJoint).getConfidence() != 0 || 
    			dict.get(movingJoint).getConfidence() != 0)
    	{
    		Point3D pos1 = dict.get(midJoint).getPosition();
    		Point3D pos2 = dict.get(anchorJoint).getPosition();
    		Point3D pos3 = dict.get(movingJoint).getPosition();
    		
    		double p12 = Math.sqrt(Math.pow((pos1.getX() - pos2.getX()),2) 
    				+ Math.pow((pos1.getY() - pos2.getY()),2));
    		double p13 = Math.sqrt(Math.pow((pos1.getX() - pos3.getX()),2) 
    				+ Math.pow((pos1.getY() - pos3.getY()),2));
    		double p23 = Math.sqrt(Math.pow((pos2.getX() - pos3.getX()),2) 
    				+ Math.pow((pos2.getY() - pos3.getY()),2));

    		double insideacos = (Math.pow(p12,2) + Math.pow(p13,2) - Math.pow(p23,2))
    				/ (2 * p12 * p13);
    		double radAng = Math.acos(insideacos);
    		double degAng = 180 - radAng*180/Math.PI;
    		
    		//System.out.println("Angle: " + degAng + " degrees");
    		rightNow = System.currentTimeMillis();
    		timeSinceLast = LongSubtraction.subAndCheck(rightNow, lastEventTime);
    		if ((timeSinceLast > 10000) && possibleBoredom){
    			System.out.println(slowPhrases[rand.nextInt(slowPhrases.length-1)]);
    			possibleBoredom = false;
    		}
    		enoughElapsedTime = timeSinceLast > 1000;
    		if ((degAng < 10) && !returnedToStart){
    			returnedToStart = true;
    			returning();
    		}
    		if ((degAng > goal) && (lastEventTime == 0 || enoughElapsedTime)
    				&& returnedToStart) {
    			doingAction();
    			returnedToStart = false;
    			possibleBoredom = true;
    			lastEventTime = rightNow;
    			rep += 1;
    			if (rep == reps)
    			{
    				rep = 0;
    				set += 1;
    				System.out.println(set + " sets down, " + (sets-set) + " to go!");
    				if (set == sets){
    	    			System.out.println("Congratulations! You're done for the day");
    	    			set = 0;
    				}
    			}
    			else if (rep == reps-2){
    				System.out.println(encPhrases[rand.nextInt(encPhrases.length-1)]);
    			}
    		}
    	}
    }
      
    public void paint(Graphics g)
    {
    	if (drawPixels)
    	{
            DataBufferByte dataBuffer = new DataBufferByte(imgbytes, width*height*3);

            WritableRaster raster = Raster.createInterleavedRaster(dataBuffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null); 

            ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

            bimg = new BufferedImage(colorModel, raster, false, null);

    		g.drawImage(bimg, 0, 0, null);
    	}
        try
		{
			int[] users = userGen.getUsers();
			for (int i = 0; i < users.length; ++i)
			{
		    	Color c = colors[users[i]%colors.length];
		    	c = new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());

		    	g.setColor(c);
				if (drawSkeleton && skeletonCap.isSkeletonTracking(users[i]))
				{
					drawSkeleton(g, users[i]);
				}
				
				if (printID)
				{
					Point3D com = depthGen.convertRealWorldToProjective(userGen.getUserCoM(users[i]));
					String label = null;
					if (!printState)
					{
						label = new String(""+users[i]);
					}
					else if (skeletonCap.isSkeletonTracking(users[i]))
					{
						// Tracking
						label = new String(users[i] + " - Tracking");
					}
					else if (skeletonCap.isSkeletonCalibrating(users[i]))
					{
						// Calibrating
						label = new String(users[i] + " - Calibrating");
					}
					else
					{
						// Nothing
						label = new String(users[i] + " - Looking for pose (" + calibPose + ")");
					}

					g.drawString(label, (int)com.getX(), (int)com.getY());
				}
			}
		} catch (StatusException e)
		{
			e.printStackTrace();
		}
    }
    
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    private void fireAfterValueChanged() {
      for (Listener listener : listeners) {
        listener.afterValueChanged(this);
      }
    }

    public boolean getValue() {
      return didAction;
    }

    public void doingAction() {
    	didAction = true;
    	fireAfterValueChanged();
    }
    
    public void returning() {
    	didAction = false;
    	fireAfterValueChanged();
    }

    public void addListener(Listener listener) {
      listeners.add(listener);
    }

    public void removeListener(Listener listener) {
      listeners.remove(listener);
    }

    public interface Listener {
      void afterValueChanged(AngleTracker app);
    }
}

